    public BTReadMessageState addData() throws BadBTMessageException {
        if (writeExpected) return null;
        if (length < 9) throw new BadBTMessageException("piece too short");
        if (buf.size() < 4 && (chunkId < 0 || offset < 0)) return null;
        if (chunkId < 0) {
            long newId = buf.getInt();
            if (newId > Integer.MAX_VALUE) throw new BadBTMessageException("unsupported bit chunk id");
            chunkId = (int) newId;
            return this;
        }
        if (offset < 0) {
            offset = buf.getInt();
            currentOffset = offset;
            complete = new BTInterval(offset, offset + length - 9, chunkId);
            welcome = readerState.getHandler().startReceivingPiece(complete);
        }
        int available = getAmountLeft();
        if (available == 0) return null;
        if (welcome) {
            if (!writeExpected) {
                writeExpected = true;
                readerState.getHandler().handlePiece(this);
            }
        } else {
            buf.discard(available);
            currentOffset += available;
            available = 0;
        }
        if (currentOffset + available == complete.getHigh() + 1) {
            readerState.getHandler().finishReceivingPiece();
            if (!writeExpected) return readerState.getEntryState();
        }
        return null;
    }

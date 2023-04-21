    private void getNodeValue(ByteArrayOutputStream os, RecordPos rec, boolean firstCall) {
        boolean foundNext = false;
        do {
            if (rec.offset > rec.page.getPageHeader().getDataLength()) {
                final long nextPage = rec.page.getPageHeader().getNextDataPage();
                if (nextPage < 0) {
                    LOG.warn("bad link to next page");
                    return;
                }
                rec.page = getCurrentPage(nextPage);
                dataCache.add(rec.page);
                rec.offset = 2;
            }
            rec.tid = ByteConversion.byteToShort(rec.page.data, rec.offset - 2);
            if (ItemId.isLink(rec.tid)) {
                rec.offset += 10;
            } else foundNext = true;
        } while (!foundNext);
        int len = ByteConversion.byteToShort(rec.page.data, rec.offset);
        rec.offset += 2;
        if (ItemId.isRelocated(rec.tid)) rec.offset += 8;
        byte[] data = rec.page.data;
        int readOffset = rec.offset;
        if (len == OVERFLOW) {
            final long op = ByteConversion.byteToLong(data, rec.offset);
            data = getOverflowValue(op);
            len = data.length;
            readOffset = 0;
            rec.offset += 8;
        }
        final short type = Signatures.getType(data[readOffset]);
        switch(type) {
            case Node.ELEMENT_NODE:
                final int children = ByteConversion.byteToInt(data, readOffset + 1);
                final byte attrSizeType = (byte) ((data[readOffset] & 0x0C) >> 0x2);
                final short attributes = (short) Signatures.read(attrSizeType, data, readOffset + 5);
                rec.offset += len + 2;
                for (int i = 0; i < children; i++) {
                    getNodeValue(os, rec, false);
                    if (children - attributes > 1) os.write((byte) ' ');
                }
                return;
            case Node.TEXT_NODE:
                os.write(data, readOffset + 1, len - 1);
                break;
            case Node.ATTRIBUTE_NODE:
                if (firstCall) {
                    final byte idSizeType = (byte) (data[readOffset] & 0x3);
                    final boolean hasNamespace = (data[readOffset] & 0x10) == 0x10;
                    int next = Signatures.getLength(idSizeType) + 1;
                    if (hasNamespace) {
                        next += 2;
                        final short prefixLen = ByteConversion.byteToShort(data, readOffset + next);
                        next += prefixLen + 2;
                    }
                    os.write(rec.page.data, readOffset + next, len - next);
                }
                break;
        }
        if (len != OVERFLOW) rec.offset += len + 2;
    }

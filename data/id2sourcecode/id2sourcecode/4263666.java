    public void write(Tag tag, RandomAccessFile raf, RandomAccessFile rafTemp) throws CannotWriteException, IOException {
        FileChannel fc = raf.getChannel();
        ByteBuffer tagBuffer = tc.convert(tag, 0);
        if (!tagExists(raf)) {
            fc.position(fc.size());
            fc.write(tagBuffer);
        } else {
            raf.seek(raf.length() - 32 + 8);
            byte[] b = new byte[4];
            raf.read(b);
            int version = Utils.getNumber(b, 0, 3);
            if (version != 2000) {
                throw new CannotWriteException("APE Tag other than version 2.0 are not supported");
            }
            b = new byte[4];
            raf.read(b);
            long oldSize = Utils.getLongNumber(b, 0, 3) + 32;
            int tagSize = tagBuffer.capacity();
            if (oldSize <= tagSize) {
                System.err.println("Overwriting old tag in mpc file");
                fc.position(fc.size() - oldSize);
                fc.write(tagBuffer);
            } else {
                System.err.println("Shrinking mpc file");
                FileChannel tempFC = rafTemp.getChannel();
                tempFC.position(0);
                fc.position(0);
                tempFC.transferFrom(fc, 0, fc.size() - oldSize);
                tempFC.position(tempFC.size());
                tempFC.write(tagBuffer);
            }
        }
    }

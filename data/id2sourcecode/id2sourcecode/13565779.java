    public static Block readBlock(int blockId, Cursor cursor, int numKeys) throws DatabaseException {
        DatabaseEntry key = new DatabaseEntry();
        DatabaseEntry data = new DatabaseEntry();
        Adler32 adler32 = new Adler32();
        int i = 0;
        int a = 0, b = 0;
        md.reset();
        Block block = new Block(blockId);
        while ((i < numKeys) && (cursor.getNext(key, data, LockMode.DEFAULT) == OperationStatus.SUCCESS)) {
            if (i == 0) {
                block.setBeginKey(key.getData());
                block.setBeginData(data.getData());
            }
            adler32.reset();
            adler32.update(key.getData(), 0, key.getData().length);
            adler32.update(data.getData(), 0, data.getData().length);
            final int xi = (int) adler32.getValue();
            a += xi;
            b += a;
            md.update(key.getData());
            md.update(data.getData());
            i++;
        }
        long cksum = (a & LDiffUtil.MASK_32BIT) | ((long) b << 32);
        block.setRollingChksum(cksum);
        block.setMd5Hash(md.digest());
        block.setNumRecords(i);
        return block;
    }

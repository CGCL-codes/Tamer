    private void fillOutputStream(OutputStream stream, int length) throws AsnException, IOException {
        byte[] dataBucket = new byte[DATA_BUCKET_SIZE];
        int readCount;
        while (length != 0) {
            int cnt = length < DATA_BUCKET_SIZE ? length : DATA_BUCKET_SIZE;
            readCount = read(dataBucket, 0, cnt);
            if (readCount < cnt) throw new AsnException("input stream has reached the end");
            stream.write(dataBucket, 0, readCount);
            length -= readCount;
        }
    }

    public byte[] strong() {
        if (data.length - index < 1) {
            return MD5.digest(data);
        } else {
            int chunkSize = Math.min(blockSize, data.length - index);
            byte[] chunk = new byte[chunkSize];
            System.arraycopy(data, index, chunk, 0, chunkSize);
            return MD5.digest(chunk);
        }
    }

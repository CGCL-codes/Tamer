    public void hashChunks() throws NoSuchAlgorithmException, IOException {
        int number = 0;
        long start = 0;
        byte[] buffer = new byte[CHUNK_SIZE];
        while (true) {
            int size = in.read(buffer);
            if (size == -1) break;
            String md5 = calculateMd5(buffer, size);
            Chunk chunk = new Chunk(md5, number, start, size);
            chunk.setAvailable();
            chunks.add(chunk);
            start += size;
            number++;
        }
        buffer = null;
        hash = digestToString(total.digest());
        Chunk[] chunks = getChunks();
        for (int i = 0; i < chunks.length; i++) {
            chunks[i].setFileHash(hash);
        }
    }

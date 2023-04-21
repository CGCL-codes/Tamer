    @Override
    public void put(String key, InputStream in) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            byte[] data = new byte[BUFFER_SIZE];
            int nbread;
            while ((nbread = in.read(data)) != -1) os.write(data, 0, nbread);
        } finally {
            in.close();
            os.close();
        }
        mCache.put(key, os.toByteArray());
    }

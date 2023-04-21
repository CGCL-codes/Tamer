    @Override
    protected Map<HashType, String> doInBackground() throws Exception {
        Hash hash = hashType.newHash();
        long length = file.length();
        InputStream in = new FileInputStream(file);
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            long position = 0;
            int len = 0;
            while ((len = in.read(buffer)) >= 0) {
                position += len;
                hash.update(buffer, 0, len);
                setProgress((int) ((position * 100) / length));
                if (isCancelled() || Thread.interrupted()) {
                    throw new CancellationException();
                }
            }
        } finally {
            in.close();
        }
        return Collections.singletonMap(hashType, hash.digest());
    }

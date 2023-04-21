    @Override
    public void put(String key, InputStream in) throws IOException {
        File f = new File(mStoreDir, key);
        OutputStream os = new BufferedOutputStream(new FileOutputStream(f));
        try {
            byte[] data = new byte[1024];
            int nbread;
            while ((nbread = in.read(data)) != -1) os.write(data, 0, nbread);
        } finally {
            in.close();
            os.close();
        }
    }

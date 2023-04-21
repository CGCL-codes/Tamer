    @Override
    public byte[] getAvatar() throws IOException {
        if (mUrl == null) mUrl = new URL(mUrlString);
        InputStream in = mUrl.openStream();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            byte[] data = new byte[1024];
            int nbread;
            while ((nbread = in.read(data)) != -1) {
                os.write(data, 0, nbread);
            }
        } finally {
            in.close();
            os.close();
        }
        return os.toByteArray();
    }

    @Override
    public void put(String key, InputStream in) throws IOException {
        Uri uri = AvatarProvider.CONTENT_URI.buildUpon().appendPath(key).build();
        OutputStream os = new BufferedOutputStream(mContentResolver.openOutputStream(uri));
        try {
            byte[] data = new byte[1024];
            int nbread;
            while ((nbread = in.read(data)) != -1) os.write(data, 0, nbread);
        } finally {
            in.close();
            os.close();
        }
    }

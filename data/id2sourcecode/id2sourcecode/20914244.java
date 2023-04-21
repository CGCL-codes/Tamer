    private File getAsset(String name) throws Exception {
        FileOutputStream fos = mContext.openFileOutput(name, 0);
        InputStream is = mAssets.open(name);
        byte[] b = new byte[4096];
        int read;
        while ((read = is.read(b)) != -1) {
            fos.write(b, 0, read);
        }
        is.close();
        fos.close();
        return mContext.getFileStreamPath(name);
    }

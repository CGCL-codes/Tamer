    public void store(String cacheEntryName, InputStream inputStream, long expirationDate) {
        createCache();
        File f = new File(cacheDir, cacheEntryName + ".xml");
        try {
            BufferedInputStream is = new BufferedInputStream(inputStream);
            BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(f));
            int read;
            byte[] buffer = new byte[4096];
            while ((read = is.read(buffer)) != -1) {
                os.write(buffer, 0, read);
            }
            os.close();
            is.close();
            File fm = new File(cacheDir, cacheEntryName + ".meta");
            Properties p = new Properties();
            p.setProperty("expiration-date", Long.toString(expirationDate));
            p.store(new FileOutputStream(fm), null);
        } catch (IOException e) {
        }
    }

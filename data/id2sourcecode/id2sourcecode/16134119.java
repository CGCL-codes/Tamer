    public static File getExternalLocalFile(String url) throws IOException, MalformedURLException {
        File temp = cache.get(url);
        if (temp == null) {
            InputStream in = new URL(url).openStream();
            temp = File.createTempFile("hyts_local", ".tmp", null);
            temp.deleteOnExit();
            FileOutputStream out = new FileOutputStream(temp);
            int result;
            byte[] buf = new byte[4096];
            while ((result = in.read(buf)) != -1) {
                out.write(buf, 0, result);
            }
            in.close();
            out.close();
            cache.put(url, temp);
        }
        return temp;
    }

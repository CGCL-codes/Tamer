    public void init(String strIniPath, String strEncoding) throws IOException {
        InputStream is;
        long lLastModified;
        if (isUrl(strIniPath)) {
            URL url = new URL(strIniPath);
            URLConnection ucon = url.openConnection();
            lLastModified = ucon.getLastModified();
            is = ucon.getInputStream();
            setSource(url);
        } else {
            File file = new File(strIniPath);
            lLastModified = file.lastModified();
            is = new FileInputStream(file);
            setSource(file);
        }
        init(is, strEncoding, lLastModified);
    }

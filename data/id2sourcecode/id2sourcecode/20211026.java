    private static void download(String pFile, String pURL) throws Exception {
        URL url = new URL(pURL);
        ;
        URLConnection urlConn;
        InputStream is;
        FileOutputStream fos;
        File f = new File(pFile);
        f.createNewFile();
        urlConn = url.openConnection();
        urlConn.setDoInput(true);
        urlConn.setUseCaches(false);
        byte[] b = new byte[5120];
        int ln = 0;
        is = urlConn.getInputStream();
        fos = new FileOutputStream(f);
        while ((ln = is.read(b)) > 0) {
            fos.write(b, 0, ln);
            fos.flush();
        }
        is.close();
        fos.close();
    }

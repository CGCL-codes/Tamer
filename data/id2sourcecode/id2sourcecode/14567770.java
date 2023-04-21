    public static boolean downloadImage(final String sFileLocal, final String sFileRemote) {
        boolean b = true;
        try {
            URL url = new URL(sFileRemote);
            URLConnection conn = url.openConnection();
            conn.setDoInput(true);
            conn.setUseCaches(false);
            InputStream is = conn.getInputStream();
            FileOutputStream os = new FileOutputStream(sFileLocal);
            int c;
            while ((c = is.read()) != -1) {
                os.write(c);
            }
            is.close();
            os.close();
        } catch (Exception exc) {
            exc.printStackTrace();
            b = false;
        }
        return b;
    }

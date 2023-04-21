    public static void download(JProgressBar pgb, String strURL, File fDestination) throws MalformedURLException, IOException {
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        URL url = new URL(strURL);
        URLConnection urlc = url.openConnection();
        bis = new BufferedInputStream(urlc.getInputStream());
        fos = new FileOutputStream(fDestination);
        int i;
        int contentLength = urlc.getContentLength();
        int nbChars = 0;
        if (pgb != null) {
            pgb.setValue(0);
            pgb.setVisible(true);
        }
        while ((i = bis.read()) != -1) {
            fos.write(i);
            nbChars++;
            if (nbChars % 2000 == 0) {
                int percent = 100 * nbChars / contentLength;
                if (pgb != null) {
                    pgb.setValue(percent);
                    pgb.paintImmediately(0, 0, pgb.getWidth(), pgb.getHeight());
                }
            }
        }
        if (pgb != null) {
            pgb.setVisible(false);
        }
        fos.close();
        bis.close();
    }

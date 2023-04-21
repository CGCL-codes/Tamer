    public void start() {
        System.out.println("Applet.start()");
        try {
            String strRead = getCodeBase() + getParameter("file");
            System.out.println("trying to open: " + strRead);
            URL url = new URL(strRead);
            urlConn = url.openConnection();
            urlConn.setUseCaches(false);
            urlConn.setDoInput(true);
            urlConn.setDoOutput(false);
            InputStream is = urlConn.getInputStream();
            int iRead = 0;
            for (int i = is.read(); i != -1; i = is.read()) {
                iRead++;
            }
            System.out.println("read " + iRead + " bytes from " + strRead);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

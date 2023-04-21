    public static void copyFile(String fileURL, String fileOutputPath) throws Exception {
        log.info("FileUtilities.copyFile - fileURL = '" + fileURL + "'");
        URL url = new URL(fileURL);
        URLConnection uc = url.openConnection();
        InputStream is = null;
        try {
            log.info("FileUtilities.copyFile - url.getInputStream()... ");
            is = uc.getInputStream();
        } catch (Throwable e) {
            log.info("FileUtilities.copyFile - EXCEPTION : '" + e.getMessage() + "'");
            log.info("FileUtilities.copyFile - url.getInputStream() using proxy ... ");
            String proxyServer = StringUtilities.getNString(Properties.getProperty("application", "PROXY_SERVER"));
            String proxyPort = StringUtilities.getNString(Properties.getProperty("application", "PROXY_PORT"));
            String proxyAuthorization = StringUtilities.getNString(Properties.getProperty("application", "PROXY_AUTHORIZATION"));
            if (!proxyServer.trim().equals("")) {
                System.getProperties().put("http.proxyHost", proxyServer);
                System.getProperties().put("http.proxyPort", proxyPort);
                if (!proxyAuthorization.trim().equals("")) uc.setRequestProperty("Proxy-Authorization", "Basic " + proxyAuthorization);
                uc.setDoInput(true);
                uc.setDoOutput(true);
            }
            is = uc.getInputStream();
        }
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        byte b[] = new byte[1024];
        int c = 0;
        while ((c = is.read(b)) > 0) bo.write(b, 0, c);
        bo.flush();
        File f = new File(fileOutputPath);
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(bo.toByteArray());
    }

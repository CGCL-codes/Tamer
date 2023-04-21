    public ASPWordTextExtractor(InputStream fis, String encoding) {
        super(fis, encoding);
        if (fis == null) {
            return;
        }
        java.io.InputStreamReader isr = null;
        java.net.HttpURLConnection urlConn = null;
        try {
            java.io.File file = java.io.File.createTempFile("DCIVISION", ".doc");
            java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
            int tmpInt = 0;
            while ((tmpInt = fis.read()) != -1) {
                fos.write(tmpInt);
            }
            fos.close();
            String strURL = CONTEXT_PATH + "/WordExtractor.asp?fileLocation=" + java.net.URLEncoder.encode(file.getAbsolutePath(), encoding);
            log.debug("strURL:" + strURL);
            java.net.URL thisURL = new java.net.URL(strURL);
            urlConn = (java.net.HttpURLConnection) thisURL.openConnection();
            urlConn.connect();
            isr = new java.io.InputStreamReader(urlConn.getInputStream(), encoding);
            Html2Text parser = new Html2Text();
            parser.parse(isr);
            text = parser.getText();
            file.delete();
        } catch (Exception e) {
            log.error(e, e);
        } finally {
            try {
                isr.close();
            } catch (Exception ignore) {
            } finally {
                isr = null;
            }
            try {
                urlConn.disconnect();
            } catch (Exception ignore) {
            } finally {
                urlConn = null;
            }
        }
    }

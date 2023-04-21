    public byte[] getEncoded(X509Certificate checkCert, String url) {
        try {
            if (url == null) {
                if (checkCert == null) return null;
                url = PdfPKCS7.getCrlUrl(checkCert);
            }
            if (url == null) return null;
            URL urlt = new URL(url);
            HttpURLConnection con = (HttpURLConnection) urlt.openConnection();
            if (con.getResponseCode() / 100 != 2) {
                throw new IOException(MessageLocalization.getComposedMessage("invalid.http.response.1", con.getResponseCode()));
            }
            InputStream inp = (InputStream) con.getContent();
            byte[] buf = new byte[1024];
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            while (true) {
                int n = inp.read(buf, 0, buf.length);
                if (n <= 0) break;
                bout.write(buf, 0, n);
            }
            inp.close();
            return bout.toByteArray();
        } catch (Exception ex) {
            if (LOGGER.isLogging(Level.ERROR)) LOGGER.error("CrlClientImp", ex);
        }
        return null;
    }

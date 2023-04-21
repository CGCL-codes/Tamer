    private String fetchExternalIpProviderHTML(String externalIpProviderUrl) {
        InputStream in = null;
        HttpURLConnection httpConn = null;
        try {
            URL url = new URL(externalIpProviderUrl);
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setConnectTimeout(20000);
            httpConn.setReadTimeout(10000);
            HttpURLConnection.setFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)");
            in = httpConn.getInputStream();
            byte[] bytes = new byte[1024];
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length && (numRead = in.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
            String receivedString = new String(bytes, "UTF-8");
            return receivedString;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                httpConn.disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

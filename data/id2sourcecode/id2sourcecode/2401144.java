    private static void downloadFile(String endpoint, String requestParameters, String localFilename) throws Exception {
        if (endpoint.startsWith("http://")) {
            StringBuffer data = new StringBuffer();
            String urlStr = prepareUrl(endpoint, requestParameters);
            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            byte[] mybytearray = new byte[1024];
            InputStream is = conn.getInputStream();
            FileOutputStream fos = new FileOutputStream(localFilename);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            int bytesRead;
            while ((bytesRead = is.read(mybytearray, 0, mybytearray.length)) >= 0) {
                bos.write(mybytearray, 0, bytesRead);
            }
            bos.close();
        }
    }

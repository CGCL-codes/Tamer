    private String httpGet(URL url) throws IOException {
        String result = null;
        if ("http".equals(url.getProtocol())) {
            log.println("Getting attributes printout from: " + url);
            InputStream in = url.openStream();
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int bytesRead, totalBytes = 0;
            while ((bytesRead = in.read(buffer, 0, buffer.length)) >= 0) {
                baos.write(buffer, 0, bytesRead);
                totalBytes += bytesRead;
            }
            in.close();
            result = baos.toString();
            log.println("Got " + totalBytes + " bytes.");
        }
        return result;
    }

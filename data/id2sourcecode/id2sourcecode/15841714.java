    public void downloadUrl(String urlString, String fileName) throws IOException {
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        connection.setAllowUserInteraction(false);
        connection.setDoOutput(true);
        connection.addRequestProperty("User-Agent", userAgent);
        InputStream in = connection.getInputStream();
        Map<String, List<String>> headers = connection.getHeaderFields();
        String headerFileName = this.getHeaderFilename(headers);
        if (headerFileName != null && !"".equals(headerFileName)) {
            fileName = this.getDownloadPath() + headerFileName;
        }
        ByteArrayOutputStream tmpOut = new ByteArrayOutputStream();
        byte[] buf = new byte[512];
        while (true) {
            int len = in.read(buf);
            if (len == -1) {
                break;
            }
            tmpOut.write(buf, 0, len);
        }
        in.close();
        log.debug("Writing output to file : {}", fileName);
        FileOutputStream fos = new FileOutputStream(fileName);
        fos.write(tmpOut.toByteArray());
        fos.close();
    }

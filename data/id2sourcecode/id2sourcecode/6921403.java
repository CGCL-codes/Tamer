    public void downloadImage(File imageFile, String imageURL) throws IOException {
        if (mjbProxyHost != null) {
            System.getProperties().put("proxySet", "true");
            System.getProperties().put("proxyHost", mjbProxyHost);
            System.getProperties().put("proxyPort", mjbProxyPort);
        }
        URL url = new URL(imageURL);
        URLConnection cnx = url.openConnection();
        if (mjbProxyUsername != null) {
            cnx.setRequestProperty("Proxy-Authorization", mjbEncodedPassword);
        }
        sendHeader(cnx);
        readHeader(cnx);
        FileTools.copy(cnx.getInputStream(), new FileOutputStream(imageFile));
    }

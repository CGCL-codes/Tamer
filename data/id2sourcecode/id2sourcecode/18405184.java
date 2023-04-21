    public void setPic(URL url) throws IOException {
        imageData = new byte[url.openConnection().getContentLength()];
        url.openStream().read(imageData, 0, imageData.length);
        icon = null;
    }

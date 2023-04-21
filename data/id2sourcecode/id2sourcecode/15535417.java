    @SuppressWarnings("serial")
    public Resource createImage() {
        StreamResource image = new StreamResource(new StreamSource() {

            @Override
            public InputStream getStream() {
                try {
                    return url.openStream();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }, url.getFile(), ApplicationWrapper.getInstance());
        return image;
    }

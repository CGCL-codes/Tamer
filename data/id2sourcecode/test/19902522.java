    static Image loadPreview(ItemType it) {
        PictureDetailsType pics = it.getPictureDetails();
        String urlname = pics.getGalleryURL();
        try {
            URL url = new URL(urlname);
            log.info("PROXY?" + System.getProperty("http.proxyHost"));
            InputStream stream = url.openStream();
            ImageLoader loader = new ImageLoader();
            ImageData[] gal = loader.load(stream);
            return new Image(Display.getCurrent(), gal[0]);
        } catch (Exception e) {
            return null;
        }
    }

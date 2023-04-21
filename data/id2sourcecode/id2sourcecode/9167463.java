    public static Image image(URL url) throws IOException {
        Image[] images = imageMap.get(url);
        if (Util.empty(images)) {
            images = new Image[1];
            images[0] = new Image(Display.getCurrent(), url.openStream());
            imageMap.put(url, images);
        }
        return images[0];
    }

    public JSmoothResources(Display display) {
        Class clazz = getClass();
        URL url = clazz.getResource("jsmooth.properties");
        try {
            bundle = new PropertyResourceBundle(url.openStream());
        } catch (IOException e) {
        }
        loadImages(this.display = display);
        loadText();
    }

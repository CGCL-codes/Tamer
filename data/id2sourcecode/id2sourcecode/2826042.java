    public DefaultHandlerString() {
        idiom = new Properties();
        String file = "/resources/languages/idiom_en_US.properties";
        URL url = getClass().getResource(file);
        try {
            idiom.load(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
            idiom = null;
        }
    }

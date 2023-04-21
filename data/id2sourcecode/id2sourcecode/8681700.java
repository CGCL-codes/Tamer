    private static Image loadImage(String strUrl) {
        try {
            URL url = ClassLoader.getSystemClassLoader().getResource(ICON_DIR + "/" + strUrl);
            return new Image(Display.getCurrent(), url.openStream());
        } catch (Exception e) {
            Logger.defaultLogger().error(e);
            return null;
        }
    }

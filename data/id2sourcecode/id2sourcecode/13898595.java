    public static void loadWindowProperties() {
        try {
            prop.load(url.openStream());
        } catch (FileNotFoundException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }
    }

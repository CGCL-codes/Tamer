    @Override
    public Image createImage() {
        try {
            return new Image(Display.getCurrent(), url.openStream());
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

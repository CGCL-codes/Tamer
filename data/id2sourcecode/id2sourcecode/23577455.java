    public WidgetTheme(URL url) throws IOException {
        this(loadThemeProps(url.openStream()));
        TextureLoader.getInstance().addTextureStreamLocator(new TextureStreamLocatorZip(url, "textures/"));
    }

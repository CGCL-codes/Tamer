    public AboutBox createAboutBox(Frame owner) {
        ClassLoader simpleClassLoader = makeInitialClassloader();
        String about = "";
        try {
            URL url = simpleClassLoader.getResource("META-INF/ABOUT.NFO");
            if (url != null) about = Tools.readTextFile(new InputStreamReader(url.openStream()));
        } catch (Exception e) {
            LogService.getRoot().log(Level.WARNING, "Error reading ABOUT.NFO for plugin " + getName(), e);
        }
        Image productLogo = null;
        try {
            InputStream imageIn = simpleClassLoader.getResourceAsStream("META-INF/icon.png");
            productLogo = ImageIO.read(imageIn);
        } catch (Exception e) {
            LogService.getRoot().log(Level.WARNING, "Error reading icon.png for plugin " + getName(), e);
        }
        return new AboutBox(owner, name, version, "Vendor: " + (vendor != null ? vendor : "unknown"), url, about, true, productLogo);
    }

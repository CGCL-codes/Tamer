    private Image getImage(IConfigurationElement configurationElement) {
        String name = configurationElement.getName();
        String icon = configurationElement.getAttribute(ICON);
        String id = configurationElement.getAttribute(ID);
        if (icon == null) {
            return getDefaultImage(name);
        }
        if (icon.startsWith(NL)) {
            icon = icon.substring(NL.length());
        }
        ImageRegistry imageRegistry = EscriptsPlugin.getDefault().getImageRegistry();
        if (imageRegistry.getDescriptor(name + ':' + id) == null) {
            Bundle bundle = Platform.getBundle(configurationElement.getNamespace());
            URL url = bundle.getEntry(icon);
            if (url != null) {
                InputStream data = null;
                try {
                    data = url.openStream();
                    Image image = new Image(Display.getDefault(), data);
                    imageRegistry.put(name + ':' + id, image);
                    return image;
                } catch (IOException exception) {
                    return getDefaultImage(name);
                } finally {
                    if (data != null) {
                        try {
                            data.close();
                        } catch (IOException couldNotClose) {
                        }
                    }
                }
            }
        }
        return getImage(name + ':' + id);
    }

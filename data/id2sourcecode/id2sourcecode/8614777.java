    @Override
    public void linkClicked(HTMLElement linkNode, URL url, String target) {
        if (Utilities.isDesktopSupported()) {
            try {
                Utilities.browseURL(url.toURI());
            } catch (URISyntaxException e) {
            }
        }
    }

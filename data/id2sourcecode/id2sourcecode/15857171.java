    public void makeLoadingImage(Map map) {
        try {
            URL url = this.getClass().getResource("mapviewer/resources/loading.png");
            map.setLoadingImage(new Image(Display.getCurrent(), url.openStream()));
        } catch (Exception ex) {
            int tileSize = map.getTileFactory().getTileSize();
            Image img = new Image(Display.getCurrent(), tileSize, tileSize);
            GC gc = new GC(img);
            gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
            gc.drawString(Messages.geoclipse_extensions_loading, 5, 5);
            gc.dispose();
            map.setLoadingImage(img);
        }
    }

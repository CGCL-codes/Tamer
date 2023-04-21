    private void makeErrorImage(Map map) {
        try {
            URL url = this.getClass().getResource("mapviewer/resources/failed.png");
            map.setFailedImage(new Image(Display.getCurrent(), url.openStream()));
        } catch (Exception ex) {
            int tileSize = map.getTileFactory().getTileSize();
            map.setFailedImage(new Image(Display.getCurrent(), tileSize, tileSize));
            GC gc = new GC(map.getFailedImage());
            gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
            gc.drawString(Messages.geoclipse_extensions_loading_failed, 5, 5);
            gc.dispose();
        }
    }

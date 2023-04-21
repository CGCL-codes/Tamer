    private final Image createActiveTabImage(Display dpl) {
        Image img = null;
        URL url = PalobrowserPlugin.getDefault().getBundle().getResource("icons/check_simple.gif");
        if (url != null) {
            try {
                ImageData imgData = new ImageData(url.openStream());
                RGB newRgb = toolkit.getColors().getColor(IFormColors.TITLE).getRGB();
                if (!imgData.palette.isDirect) {
                    RGB[] rgbs = imgData.palette.colors;
                    for (int i = 0; i < rgbs.length; i++) {
                        RGB rgb = rgbs[i];
                        if (rgb.blue < 10 && rgb.red < 10 && rgb.green < 10) {
                            rgbs[i] = newRgb;
                        }
                    }
                }
                img = new Image(dpl, imgData);
            } catch (IOException e) {
            }
        }
        if (img == null) img = PalobrowserPlugin.getDefault().getImageRegistry().get("icons/check_simple.gif");
        return img;
    }

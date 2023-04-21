    private Image loadImage() {
        Image newImage = null;
        try {
            newImage = new Image(display, "./resources/mouse4text.gif");
        } catch (Exception e) {
            java.net.URL url = this.getClass().getResource("/resources/mouse4text.gif");
            InputStream stream = null;
            try {
                stream = url.openStream();
                newImage = (new Image(display, stream));
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return newImage;
    }

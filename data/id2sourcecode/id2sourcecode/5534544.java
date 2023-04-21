    private void loadImage(File f) throws IOException {
        if (file != null) {
            decoder.read(file.toURI().toURL().openStream());
        } else if (url != null) {
            decoder.read(new URL(url).openStream());
        }
        image = new BufferedImage(decoder.getImage().getWidth(), decoder.getImage().getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        System.out.println("Image Count: " + decoder.getFrameCount());
        new Thread(new Runnable() {

            @Override
            public void run() {
                updateImage();
            }
        }).start();
    }

    public Image getImage(InputStream is) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int i = 0;
        byte buf[] = new byte[1024];
        while ((i = is.read(buf)) != -1) out.write(buf, 0, i);
        byte fileBytes[] = out.toByteArray();
        Image image = Toolkit.getDefaultToolkit().createImage(fileBytes);
        MediaTracker mediaTracker = new MediaTracker(new Container());
        mediaTracker.addImage(image, 0);
        try {
            mediaTracker.waitForID(0);
        } catch (InterruptedException e) {
            logger.error("Unsuccessful attempt to load image input stream from webdav", e);
        }
        return image;
    }

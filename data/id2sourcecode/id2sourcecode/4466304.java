    public void setCommandContext(String verb, DataHandler dh) throws IOException {
        InputStream in = dh.getInputStream();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        for (int len = in.read(buf); len != -1; len = in.read(buf)) bytes.write(buf, 0, len);
        in.close();
        Toolkit toolkit = getToolkit();
        Image img = toolkit.createImage(bytes.toByteArray());
        try {
            MediaTracker tracker = new MediaTracker(this);
            tracker.addImage(img, 0);
            tracker.waitForID(0);
        } catch (InterruptedException e) {
        }
        toolkit.prepareImage(img, -1, -1, this);
    }

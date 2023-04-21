    private Image _image(String name) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image img = null;
        try {
            MediaTracker mt = new MediaTracker(_c);
            InputStream in = io.RootDir.getResourceAsStream(name);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int read = in.read();
            while (read != -1) {
                out.write(read);
                read = in.read();
            }
            in.close();
            byte[] buffer = out.toByteArray();
            img = Toolkit.getDefaultToolkit().createImage(buffer);
            mt.addImage(img, 0);
            mt.waitForID(0);
        } catch (Exception e) {
            System.err.println("Unable to read image.");
            e.printStackTrace();
        }
        _x = img.getWidth(null);
        _y = img.getHeight(null);
        return img;
    }

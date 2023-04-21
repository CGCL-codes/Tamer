    public int load_image(URL url) {
        String nombre = url.getFile();
        String ext = nombre.substring(nombre.length() - 3, nombre.length());
        BufferedImage image = null;
        try {
            InputStream fin = url.openStream();
            DataInputStream file = new DataInputStream(new BufferedInputStream(fin));
            if (ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("gif")) {
                image = ImageIO.read(file);
            } else {
                image = ImageIO.read(file);
                image = GraphicsUtil.toCompatibleImage(MaskColorImage.maskImage(image));
            }
            JFpgImage jfpgi = new JFpgImage(image, 100 + images.size(), image.getHeight() * image.getWidth(), "Imagen " + images.size(), nombre, image.getWidth(), image.getHeight(), 0, new ArrayList<JFlag>());
            images.add(jfpgi);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MAX_FPG_IMAGE + (images.size() - 1);
    }

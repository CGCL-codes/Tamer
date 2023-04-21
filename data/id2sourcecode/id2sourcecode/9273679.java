    Pair<JImage, byte[]> getImage(String path, int width, boolean download) {
        if (!isDirAllowed(getDirectory(path))) return null;
        try {
            String name = path.substring(path.lastIndexOf("/") + 1);
            if (name.contains(".")) name = name.substring(0, name.lastIndexOf("."));
            File f = new File(base + "/" + path);
            if (f.exists()) {
                byte[] data;
                long time = System.currentTimeMillis();
                FileInputStream fs = new FileInputStream(f);
                if (download) {
                    int read = 0;
                    byte[] chunk = new byte[org.magnesia.Constants.CHUNK_SIZE];
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    while ((read = fs.read(chunk)) > 0) {
                        bos.write(chunk, 0, read);
                    }
                    fs.close();
                    data = bos.toByteArray();
                } else {
                    ImageReader reader = ImageIO.getImageReadersByMIMEType("image/jpeg").next();
                    ImageInputStream iis = ImageIO.createImageInputStream(fs);
                    reader.setInput(iis);
                    BufferedImage orig = reader.read(0);
                    reader.dispose();
                    fs.close();
                    BufferedImage bi = Utils.drawScaled(orig, (width > 0 && width < orig.getWidth()) ? width : orig.getWidth());
                    data = Utils.getData(bi);
                    log("Time needed to load, scale and draw image: " + (System.currentTimeMillis() - time) + "ms");
                }
                return new Pair<JImage, byte[]>(new JImage(path.substring(0, path.lastIndexOf("/")), path.substring(path.lastIndexOf("/") + 1), f.length()), data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

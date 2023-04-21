    public static ImageFileShape createImageFileShape(String path) {
        try {
            ImageFileShape shape = new ImageFileShape();
            shape.setSize(new Dimension(16, 16));
            shape.setBackColor(new RGB(255, 255, 255));
            URL url = FileLocator.find(ShapesPlugin.getDefault().getBundle(), new Path(path), null);
            String s0 = path;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            TousleUtil.copyIntput2Output(url.openStream(), baos);
            String s1 = new Base64Encoder().encode(baos.toByteArray());
            shape.setFileEmbed(new String[] { s0, s1 });
            return shape;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

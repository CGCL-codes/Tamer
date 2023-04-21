    public static ImageIcon getExternalImageAsIcon(String imageFile) {
        try {
            FileInputStream in = new FileInputStream(imageFile);
            final byte[] imageByteBuffer = convertToByteArray(in);
            in.close();
            return new ImageIcon(imageByteBuffer);
        } catch (Exception e) {
            return null;
        }
    }

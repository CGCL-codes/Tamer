    public static boolean writeImageToJPG(File file, BufferedImage bufferedImage) throws IOException {
        if (readImageFromFile(file) == null) return ImageIO.write(bufferedImage, "jpg", file); else return true;
    }

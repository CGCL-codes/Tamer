    public static boolean writeImageToFile(File file, BufferedImage bufferedImage) throws IOException {
        if (file == null) return false;
        String strExtn = getFileExtension(file);
        if (readImageFromFile(file) == null) return ImageIO.write(bufferedImage, strExtn, file); else return true;
    }

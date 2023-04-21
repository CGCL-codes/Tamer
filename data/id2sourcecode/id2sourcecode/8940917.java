    public static void createRawThumbnail(String fileName, File thumbnailFile, Float thumbnailSize) throws IOException, FileNotFoundException {
        ImageUtils iu = new ImageUtils();
        iu.load(fileName);
        if (iu.smoothRawThumbnail(thumbnailSize)) {
            FileOutputStream fOut = new FileOutputStream(thumbnailFile);
            ImageIO.write(iu.getModifiedImage(), "jpg", fOut);
            fOut.flush();
            fOut.close();
        } else {
            File originalFile = new File(fileName);
            FileUtils.copyFile(originalFile, thumbnailFile);
        }
    }

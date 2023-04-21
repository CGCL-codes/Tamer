    private static FileSystemEntry generateOsdCover(BufferedImage image) throws IOException {
        BufferedImage osdImage = image;
        osdImage = GraphicTools.createReflectedPicture(osdImage, "posters");
        osdImage = GraphicTools.create3DPicture(osdImage, "posters", "right");
        osdImage = GraphicTools.scaleToSizeBestFit(350, 590, osdImage);
        File file = new File("./tmp.jpg");
        ImageIO.write(osdImage, "JPG", file);
        String md5Name = FtpHosting.hashFileForFtp(file);
        String url = FtpHosting.getUrl(OSD_COVER_JPG_SUFFIX + "/" + md5Name);
        FileUtils.copyFile(file, new File(FtpHosting.getLocalPath(OSD_COVER_JPG_SUFFIX + "/" + md5Name)));
        FileSystemEntry entry = new FileSystemEntry();
        entry.setType(Type.REMOTE);
        entry.setContentLength(file.length());
        entry.setUrl(url);
        FileUtils.deleteQuietly(file);
        return entry;
    }

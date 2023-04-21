    private static void generateOsdCover(JSONObject movie, BufferedImage image) throws IOException, JSONException {
        BufferedImage osdImage = image;
        osdImage = GraphicTools.createReflectedPicture(osdImage, "posters");
        osdImage = GraphicTools.create3DPicture(osdImage, "posters", "right");
        osdImage = GraphicTools.scaleToSizeBestFit(350, 590, osdImage);
        File file = new File("./tmp.jpg");
        ImageIO.write(osdImage, "JPG", file);
        String md5Name = DigestUtils.md5Hex(FileUtils.readFileToByteArray(file)).toUpperCase();
        String url = OSD_COVER_JPG_URL + "/" + md5Name;
        String urlMd5 = DigestUtils.md5Hex(url).toUpperCase();
        FileUtils.copyFile(file, new File("./ftp/files/osd/movies/images/" + md5Name));
        FileUtils.copyFile(file, new File("./atfs_cache/" + urlMd5));
        movie.put("osdcoverurl", url);
        movie.put("osdcoverurlsize", file.length());
        FileUtils.deleteQuietly(file);
    }

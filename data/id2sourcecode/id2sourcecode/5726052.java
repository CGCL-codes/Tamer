    private static void generateTrailerCovers(JSONObject movie, File imageFile) throws IOException, JSONException {
        BufferedImage image;
        try {
            image = ImageIO.read(imageFile);
        } catch (Exception ex) {
            log.error("could not read image file " + imageFile);
            return;
        }
        BufferedImage overlay = ImageIO.read(new File("./templates/overlay.png"));
        for (int i = 0; i < movie.getJSONArray("trailers").length(); i++) {
            JSONObject trailer = movie.getJSONArray("trailers").getJSONObject(i);
            log.debug("Generating thumbnail for trailer " + trailer.getString("filename"));
            String type = trailer.getString("type");
            String resolution = trailer.getString("resolution");
            BufferedImage trailerImage = new BufferedImage(120, 180, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = (Graphics2D) trailerImage.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.drawImage(image, 0, 0, null);
            g.drawImage(overlay, 0, 0, null);
            g.setFont(Font.decode("Verdana").deriveFont(30f));
            g.drawString(type, 5, 32);
            g.setFont(Font.decode("Verdana").deriveFont(26f));
            FontMetrics fm = g.getFontMetrics();
            Rectangle2D area = fm.getStringBounds(resolution, g);
            g.drawString(resolution, (int) (trailerImage.getWidth() - area.getWidth()) / 2, 170);
            File file = new File("./tmp.jpg");
            ImageIO.write(trailerImage, "JPG", file);
            String md5Name = DigestUtils.md5Hex(FileUtils.readFileToByteArray(file)).toUpperCase();
            String url = TRAILER_JPG_URL + "/" + md5Name;
            String urlMd5 = DigestUtils.md5Hex(url).toUpperCase();
            File ftpFile = new File("./ftp/files/images/trailers/" + md5Name);
            FileUtils.copyFile(file, ftpFile);
            FileUtils.copyFile(file, new File("./atfs_cache/" + urlMd5));
            trailer.put("jpgurl", url);
            trailer.put("jpgurlsize", file.length());
            FileUtils.deleteQuietly(file);
            log.debug("Generated thumbnail for trailer " + trailer.getString("filename") + " to " + ftpFile);
        }
    }

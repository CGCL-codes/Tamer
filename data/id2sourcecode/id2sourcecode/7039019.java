    private static List<FileSystemEntry> generateTrailerCovers(JSONObject movie, File imageFile) throws IOException {
        BufferedImage image;
        try {
            image = ImageIO.read(imageFile);
        } catch (Exception ex) {
            log.error("could not read image file " + imageFile);
            return null;
        }
        List<FileSystemEntry> entries = new ArrayList<FileSystemEntry>();
        BufferedImage overlay = ImageIO.read(new File("./templates/overlay.png"));
        for (int i = 0; i < movie.getJSONArray("trailers").size(); i++) {
            JSONObject trailer = movie.getJSONArray("trailers").getJSONObject(i);
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
            File file = File.createTempFile("trailercover", null);
            ImageIO.write(trailerImage, "JPG", file);
            String md5Name = FtpHosting.hashFileForFtp(file);
            String url = FtpHosting.getUrl(TRAILER_JPG_SUFFIX + "/" + md5Name);
            File ftpFile = new File(FtpHosting.getLocalPath(TRAILER_JPG_SUFFIX + "/" + md5Name));
            FileUtils.copyFile(file, ftpFile);
            FileUtils.deleteQuietly(file);
            log.debug("Generated thumbnail for trailer " + trailer.getString("filename") + " to " + ftpFile);
            FileSystemEntry entry = new FileSystemEntry();
            entry.setType(Type.REMOTE);
            entry.setFilename(changeExtension(getTrailerFilename(movie, trailer), "jpg"));
            entry.setContentLength(ftpFile.length());
            entry.setUrl(url);
            entries.add(entry);
        }
        return entries;
    }

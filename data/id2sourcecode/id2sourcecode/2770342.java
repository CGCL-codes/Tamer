    public MP3AudioMetadataExtractor() {
        try {
            defaultImage = ImageIO.read(getClass().getResource("/images/not_available.jpg"));
            if (defaultImage.getWidth() > 128) {
                defaultImage = GraphicsUtilities.createThumbnailFast(defaultImage, 128);
            }
            byte[] imageBytes = ImageUtils.toByteArray(defaultImage);
            MessageDigest md;
            try {
                md = MessageDigest.getInstance("MD5");
                md.update(imageBytes);
                byte[] hash = md.digest();
                defaultImageHash = Util.returnHex(hash);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Avatar(JID jid, byte[] imageData) throws IllegalArgumentException {
        if (jid == null || imageData == null) {
            throw new IllegalArgumentException("Avatar: Passed null argument.");
        }
        this.jid = jid;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(imageData);
            this.xmppHash = StringUtils.encodeHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            Log.error("Avatar: Unable to find support for SHA algorithm?");
        }
        this.createDate = new Date();
        this.lastUpdate = new Date();
        ImageInfo imageInfo = new ImageInfo();
        imageInfo.setInput(new ByteArrayInputStream(imageData));
        this.mimeType = imageInfo.getMimeType();
        try {
            insertIntoDb(Base64.encodeBytes(imageData));
        } catch (SQLException e) {
            Log.error("Avatar: SQL exception while inserting avatar: ", e);
        }
    }

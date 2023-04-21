    public void setIcon(final String type, final byte[] data) {
        this.pendingAvatar = data;
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(data);
            final ExtraInfoData eid = new ExtraInfoData(ExtraInfoData.FLAG_HASH_PRESENT, ByteBlock.wrap(digest.digest()));
            final SsiCommand request;
            final IconItem newIconItem;
            if (icon != null) {
                newIconItem = new IconItem(icon);
                newIconItem.setIconInfo(eid);
                request = new ModifyItemsCmd(newIconItem.toSsiItem());
            } else {
                newIconItem = new IconItem(IconItem.NAME_DEFAULT, this.getNextBuddyId(SsiItem.GROUP_ROOT), eid);
                request = new CreateItemsCmd(newIconItem.toSsiItem());
            }
            request(new PreModCmd());
            request(request);
            request(new PostModCmd());
            this.icon = newIconItem;
        } catch (NoSuchAlgorithmException e) {
            Log.error("No algorithm found for MD5 checksum??");
        }
    }

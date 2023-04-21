    public void create() throws IOException {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException nsa) {
            throw new InternalError(nsa.toString());
        }
        byte[] piece_hashes = metainfo.getPieceHashes();
        byte[] piece = new byte[piece_size];
        for (int i = 0; i < pieces; i++) {
            int length = getUncheckedPiece(i, piece, 0);
            digest.update(piece, 0, length);
            byte[] hash = digest.digest();
            for (int j = 0; j < 20; j++) {
                piece_hashes[20 * i + j] = hash[j];
            }
            bitfield.set(i);
            if (listener != null) {
                listener.storageChecked(this, i, true);
            }
        }
        if (listener != null) {
            listener.storageAllChecked(this);
        }
        metainfo = metainfo.reannounce(metainfo.getAnnounce());
    }

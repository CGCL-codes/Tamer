    protected static Identity addNewSharer(String _sharer, String _pubkey) {
        String given_digest = _sharer.substring(_sharer.indexOf("@") + 1, _sharer.length()).trim();
        String calculatedDigest = Core.getCrypto().digest(_pubkey.trim()).trim();
        calculatedDigest = Mixed.makeFilename(calculatedDigest).trim();
        if (!Mixed.makeFilename(given_digest).equals(calculatedDigest)) {
            logger.warning("Warning: public key of sharer didn't match its digest:\n" + "given digest :'" + given_digest + "'\n" + "pubkey       :'" + _pubkey.trim() + "'\n" + "calc. digest :'" + calculatedDigest + "'");
            return null;
        }
        Identity sharer = new Identity(_sharer.substring(0, _sharer.indexOf("@")), _pubkey);
        sharer.setState(FrostIdentities.NEUTRAL);
        Core.getIdentities().addIdentity(sharer);
        return sharer;
    }

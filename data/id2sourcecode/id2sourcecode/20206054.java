    public void updateManifest(String oldName, String newName, MessageDigest[] digests) {
        Attributes attrs = getManifest().getAttributes(oldName);
        if (null == attrs) {
            attrs = new Attributes();
        }
        if (digests != null && digests.length > 0) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < digests.length; i++) {
                if (i > 0) {
                    sb.append(" ");
                }
                sb.append(digests[i].getAlgorithm());
            }
            attrs.putValue(MANIFEST_DIGESTALG_TAG, sb.toString());
            for (int i = 0; i < digests.length; i++) {
                attrs.putValue(digests[i].getAlgorithm() + "-Digest", toBase64(digests[i].digest()));
            }
        }
        getManifest().getEntries().remove(oldName);
        getManifest().getEntries().put(newName, attrs);
        if (oldName.endsWith(ClassConstants.CLASS_EXT)) {
            Attributes mainAttrs = getManifest().getMainAttributes();
            if (null != mainAttrs) {
                String str = mainAttrs.getValue(Attributes.Name.MAIN_CLASS);
                int len = ClassConstants.CLASS_EXT.length();
                if (null != str && str.equals(oldName.substring(0, oldName.length() - len))) {
                    mainAttrs.put(Attributes.Name.MAIN_CLASS, newName.substring(0, newName.length() - len));
                }
            }
        }
    }

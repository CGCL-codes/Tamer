    public byte[] getFingerprint() {
        try {
            MessageDigest md;
            switch(version) {
                case 3:
                    md = MessageDigest.getInstance("MD5");
                    md.update(material[0].toByteArray());
                    md.update(material[1].toByteArray());
                    return md.digest();
                case 4:
                    md = MessageDigest.getInstance("SHA1");
                    byte[] head = { (byte) 0x99, (byte) (pkLength >> 8), (byte) pkLength };
                    md.update(head);
                    md.update(getPayload(), 0, pkLength);
                    return md.digest();
                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

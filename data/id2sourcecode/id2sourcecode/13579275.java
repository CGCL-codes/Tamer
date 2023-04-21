    public NodeId generateNodeId() {
        byte raw[] = new byte[8];
        long tmp = ++next;
        for (int i = 0; i < 8; i++) {
            raw[i] = (byte) (tmp & 0xff);
            tmp >>= 8;
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("No SHA support!");
        }
        md.update(raw);
        byte[] digest = md.digest();
        NodeId nodeId = new NodeId(digest);
        return nodeId;
    }

    public String calculateID(final Disc disc) {
        String id = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(os);
            ps.printf("%02X", disc.getFirstTrackNum());
            md.update(os.toByteArray());
            os.reset();
            ps.printf("%02X", disc.getLastTrackNum());
            md.update(os.toByteArray());
            for (int i = 0; i < 100; i++) {
                os.reset();
                if (i == 0) {
                    ps.printf("%08X", disc.getSectors());
                } else if (i > disc.getLastTrackNum()) {
                    ps.printf("%08X", 0);
                } else {
                    ps.printf("%08X", disc.getTracks().get(i - 1).getOffset());
                }
                md.update(os.toByteArray());
            }
            byte[] digest = md.digest();
            String encoded = new String(Base64.encodeBase64(digest));
            encoded = encoded.replace('/', '_');
            encoded = encoded.replace('+', '.');
            encoded = encoded.replace('=', '-');
            id = encoded;
        } catch (Exception e) {
            System.err.println("Could not compute discID");
            e.printStackTrace();
        }
        return id;
    }

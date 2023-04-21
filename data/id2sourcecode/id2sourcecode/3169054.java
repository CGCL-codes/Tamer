    private String hashFile(File f) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[65536];
            int plip = 0;
            InputStream in = new BufferedInputStream(new FileInputStream(f));
            int length = 1;
            while (true) {
                if (++plip > 16) {
                    plip = 0;
                    screen.print(".");
                }
                length = in.read(buffer);
                if (length <= 0) break;
                md.update(buffer, 0, length);
            }
            screen.println();
            return byteArrayToHexString(md.digest());
        } catch (Exception X) {
            return "invalid hash";
        }
    }

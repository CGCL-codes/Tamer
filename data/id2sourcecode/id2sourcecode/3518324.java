    public static byte[] getDerivedMD5(File input) throws IOException, GeneralSecurityException {
        InputStream is = new FileInputStream(input);
        MessageDigest md = MessageDigest.getInstance("MD5");
        try {
            int nextByte;
            outer: while ((nextByte = is.read()) != -1) {
                if (nextByte == 0x0a || nextByte == 0x0d) continue;
                for (int i = 0; i < initialField.length; i++) {
                    if (nextByte != initialField[i]) continue outer;
                    if ((nextByte = is.read()) == -1) {
                        throw new IOException("Reached end of file before discovering initial field");
                    }
                }
                for (int i = 0; i < initialField.length; i++) {
                    md.update((byte) (initialField[i] & 0xff));
                }
                break;
            }
            while ((nextByte = is.read()) != -1) {
                if (nextByte == 0x0a || nextByte == 0x0d) continue;
                md.update((byte) (nextByte & 0xff));
            }
            return md.digest();
        } finally {
            try {
                is.close();
            } catch (IOException ioe) {
            }
        }
    }

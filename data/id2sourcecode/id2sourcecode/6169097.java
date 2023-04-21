    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        if (args.length < 1) {
            System.out.println("You must supply a filename.");
            return;
        }
        MessageDigest tt = new TTH();
        FileInputStream fis;
        for (int i = 0; i < args.length; i++) {
            fis = new FileInputStream(args[i]);
            int read;
            byte[] in = new byte[1024];
            while ((read = fis.read(in)) > -1) {
                tt.update(in, 0, read);
            }
            fis.close();
            byte[] digest = tt.digest();
            String hash = new BigInteger(1, digest).toString(16);
            while (hash.length() < 48) {
                hash = "0" + hash;
            }
            System.out.println("hex:" + hash);
            System.out.println("b32:" + Base32.encode(digest));
            tt.reset();
        }
    }

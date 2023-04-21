    public static void main(String args[]) throws Exception {
        for (int i = 0; i < args.length; i++) {
            MessageDigest md;
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException nsa) {
                System.out.println("Can't get MD5 implementation " + nsa);
                return;
            }
            File f = new File(args[i]);
            FileInputStream fis = new FileInputStream(f);
            byte[] b = new byte[2048 * 1024];
            int count;
            while (0 < (count = fis.read(b))) {
                md.update(b, 0, count);
            }
            byte[] hash = md.digest();
            String hashed = byteToHex(hash);
            System.out.print(hashed);
        }
    }

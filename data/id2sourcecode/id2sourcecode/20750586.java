    public static void main(String[] args) throws NoSuchAlgorithmException {
        byte[] plaintextBytes = null;
        byte[] buff = new byte[50];
        MessageDigest md = null;
        md = MessageDigest.getInstance("MD5");
        System.out.println();
        System.out.println("INFO:");
        System.out.println("md.getAlgorithm() -> " + md.getAlgorithm());
        System.out.println("md.getDigestLength() -> " + md.getDigestLength());
        System.out.println("md.getProvider() -> " + md.getProvider());
        System.out.println("md.getClass().getName() -> " + md.getClass().getName());
        String line = null;
        while (true) {
            System.out.println();
            System.out.println("Enter text ( or * if you want to quit ):");
            line = readLn(1024);
            if (line == null) break;
            line = line.trim();
            if (line.length() == 1 && line.charAt(0) == '*') break;
            plaintextBytes = line.getBytes();
            md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(plaintextBytes);
            buff = md.digest();
            System.out.println();
            System.out.println("MD5 signature as plaintext:");
            System.out.println(new String(buff));
            System.out.println("MD5 signature as HEX string:");
            printHexString(buff);
        }
    }

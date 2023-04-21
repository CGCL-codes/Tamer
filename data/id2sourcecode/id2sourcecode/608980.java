    public static void main(String[] args) {
        try {
            FileOutputStream outFile = new FileOutputStream("MD.out");
            PrintStream output = new PrintStream(outFile);
            byte b;
            String w;
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            FileInputStream fis = new FileInputStream(args[0]);
            while (fis.available() != 0) {
                b = (byte) fis.read();
                sha.update(b);
            }
            ;
            fis.close();
            byte[] hash = sha.digest();
            w = cryptix.util.core.Hex.dumpString(hash);
            output.println("the SHA-1 hash of " + args[0] + " is " + w);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(args[0]);
            while (fis.available() != 0) {
                b = (byte) fis.read();
                md5.update(b);
            }
            ;
            fis.close();
            hash = md5.digest();
            w = cryptix.util.core.Hex.dumpString(hash);
            output.println("the MD5 hash of " + args[0] + " is " + w);
            MessageDigest rpm128 = MessageDigest.getInstance("RIPEMD128");
            fis = new FileInputStream(args[0]);
            while (fis.available() != 0) {
                b = (byte) fis.read();
                rpm128.update(b);
            }
            ;
            fis.close();
            hash = rpm128.digest();
            w = cryptix.util.core.Hex.dumpString(hash);
            output.println("the RIPEM128 hash of " + args[0] + " is " + w);
            MessageDigest rpm160 = MessageDigest.getInstance("RIPEMD160");
            fis = new FileInputStream(args[0]);
            while (fis.available() != 0) {
                b = (byte) fis.read();
                rpm160.update(b);
            }
            ;
            fis.close();
            hash = rpm160.digest();
            w = cryptix.util.core.Hex.dumpString(hash);
            output.println("the RIPEM160 hash of " + args[0] + " is " + w);
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
    }

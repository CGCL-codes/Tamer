    public static byte[] readFile(InputStream inputStream) throws IOException {
        DigestInputStream dis = null;
        try {
            dis = new DigestInputStream(inputStream, MessageDigest.getInstance("SHA1"));
        } catch (NoSuchAlgorithmException e) {
            Log.severe("No SHA1 available!");
            Assert.fail("No SHA1 available!");
        }
        int elapsed = 0;
        int read = 0;
        byte[] bytes = new byte[BUF_SIZE];
        while (true) {
            read = dis.read(bytes);
            if (read < 0) {
                System.out.println("EOF read at " + elapsed + " bytes.");
                break;
            } else if (read == 0) {
                System.out.println("0 bytes read at " + elapsed + " bytes.");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
            }
            elapsed += read;
            System.out.println(" read " + elapsed + " bytes.");
        }
        return dis.getMessageDigest().digest();
    }

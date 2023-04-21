    public static void main(String args[]) throws Exception {
        if (args.length == 1) {
            System.out.println("[" + Base64Encoder.encode(args[0]) + "]");
        } else if (args.length == 2) {
            byte[] hash = java.security.MessageDigest.getInstance(args[1]).digest(args[0].getBytes());
            System.out.println("[" + Base64Encoder.encode(hash) + "]");
        } else {
            System.out.println("Usage: Base64Encoder <string> <optional hash algorithm>");
        }
    }

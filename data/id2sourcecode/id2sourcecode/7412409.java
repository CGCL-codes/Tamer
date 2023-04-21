    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println("Usage: password_file name password");
            return;
        }
        byte[] pass = MessageDigest.getInstance("SHA-256").digest(args[2].getBytes("UTF-8"));
        byte[] encodedPass = NamePasswordAuthenticator.encodeBytes(pass);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(args[0], true);
            out.write(args[1].getBytes("UTF-8"));
            out.write("\t".getBytes("UTF-8"));
            out.write(encodedPass);
            out.write("\n".getBytes("UTF-8"));
        } finally {
            out.close();
        }
    }

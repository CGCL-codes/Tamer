    public static void main(String[] args) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            FileInputStream fis = new FileInputStream(args[0]);
            byte[] data = new byte[BUFSIZE];
            do {
                int numread = fis.read(data);
                if (numread == -1) {
                    break;
                } else {
                    sha.update(data, 0, numread);
                }
            } while (true);
            fis.close();
            byte[] hash = sha.digest();
            System.out.println(stringForm(hash));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

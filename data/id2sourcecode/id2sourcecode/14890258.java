    public String encryption(String Hash) {
        String signature64 = "";
        try {
            System.out.println("========== SHA1=========");
            byte[] buffer = Hash.getBytes();
            MessageDigest algorithm = MessageDigest.getInstance("SHA1");
            algorithm.reset();
            algorithm.update(buffer);
            Base64 base64_signature = new Base64();
            signature64 = new String(base64_signature.encode(algorithm.digest()));
            System.out.println(" 64 base=" + signature64);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return signature64;
    }

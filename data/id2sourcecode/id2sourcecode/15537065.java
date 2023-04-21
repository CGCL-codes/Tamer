    public void codifica() {
        byte testo[] = "Tutto il mio testo da decodificare".getBytes();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(testo);
            byte messageDigest[] = algorithm.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toHexString(messageDigest[i]));
            }
            String foo = messageDigest.toString();
            System.out.println("Testo codificato in md5 " + foo + " --> " + hexString.toString());
        } catch (NoSuchAlgorithmException nsae) {
        }
    }

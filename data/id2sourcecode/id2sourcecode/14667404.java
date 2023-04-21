    public static String calculaDC(String texto) throws Exception {
        byte[] datos = texto.getBytes("UTF-8");
        MessageDigest dig = MessageDigest.getInstance("SHA-512");
        String hex = new String(Hex.encodeHex(dig.digest(datos)));
        BigInteger bi = new BigInteger(hex, 16);
        String bis = bi.toString();
        int num = Integer.parseInt(bis.substring(bis.length() - 2)) % 99;
        String snum = Integer.toString(num);
        if (snum.length() < 2) snum = "0" + snum;
        return snum;
    }

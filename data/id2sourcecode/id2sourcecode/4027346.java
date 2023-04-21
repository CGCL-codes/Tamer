    public static String generarHash(String algorithm, String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] aInput = md.digest(texto.getBytes("UTF8"));
            StringBuilder result = new StringBuilder();
            char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
            for (int idx = 0; idx < aInput.length; ++idx) {
                byte b = aInput[idx];
                result.append(digits[(b & 0xf0) >> 4]);
                result.append(digits[b & 0x0f]);
            }
            return result.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Problema al generar hash");
        }
    }

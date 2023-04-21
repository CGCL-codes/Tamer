    public synchronized String encrypt(char[] chartext) throws UnsupportedEncodingException {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            throw new UnsupportedEncodingException(e.getMessage());
        } catch (Exception ex) {
            System.out.println("General Exception encoding creating the Diget");
            ex.printStackTrace();
            throw new UnsupportedEncodingException(ex.getMessage());
        }
        try {
            byte[] bytetext = encodeUTF8(chartext);
            md.update(bytetext);
            for (int i = 0; i < bytetext.length; i++) {
                chartext[i] = 0;
                bytetext[i] = 0;
            }
        } catch (Exception e) {
            for (int i = 0; i < chartext.length; i++) {
                chartext[i] = 0;
            }
            throw new UnsupportedEncodingException(e.getMessage());
        }
        byte raw[] = md.digest();
        String hash = (new BASE64Encoder()).encode(raw);
        return hash;
    }

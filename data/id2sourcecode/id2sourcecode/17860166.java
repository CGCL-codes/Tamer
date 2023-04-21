    private String createDigest() {
        StringBuffer buff = new StringBuffer();
        try {
            String key = "";
            String g = getGreeting();
            int spos = g.indexOf('<');
            int epos = g.indexOf('>');
            if (spos != -1 && epos != -1 && spos < epos) {
                key = g.substring(spos, epos + 1);
            }
            key = key + getPassword();
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(key.getBytes(ENCODE));
            for (int i = 0; i < digest.length; ++i) {
                char c = (char) ((digest[i] >> 4) & 0xf);
                if (c > 9) {
                    c = (char) ((c - 10) + 'a');
                } else {
                    c = (char) (c + '0');
                }
                buff.append(c);
                c = (char) (digest[i] & 0xf);
                if (c > 9) {
                    c = (char) ((c - 10) + 'a');
                } else {
                    c = (char) (c + '0');
                }
                buff.append(c);
            }
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
        }
        return buff.toString();
    }

    public Object getAsObject(FacesContext fc, UIComponent c, String pass_temp) throws ConverterException {
        if (pass_temp.length() < 5) {
            return pass_temp;
        } else {
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            BigInteger hash = new BigInteger(1, md.digest(pass_temp.getBytes()));
            String pass = hash.toString(16);
            return pass;
        }
    }

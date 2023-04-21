    private XCP _authenticate() {
        OPS.read_data(input);
        OPS.write_data(output, getMessage(3).getBytes());
        OPS.write_data(output, getMessage(4).getBytes());
        byte[] challenge = OPS.read_data(input);
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] hash = md.digest(challenge);
        OPS.write_data(output, _cipher.encrypt(hash));
        try {
            XCP response = new XCP(new String(read_data(true)));
            if (response.get_data("is_success").equals("1")) authenticated = true;
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

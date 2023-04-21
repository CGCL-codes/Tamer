    static String detectLanguage(File textFile) throws XmlRpcException, XmlRpcFault, TimeoutException, FileNotFoundException, IOException, NoSuchAlgorithmException, LanguageNotSupportedException, OsdbException, InterruptedException {
        if (!isLoggedIn()) {
            anonymousLogIn();
        }
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ByteArrayOutputStream byteTmp = new ByteArrayOutputStream();
        DeflaterOutputStream gzout = new DeflaterOutputStream(byteOut);
        FileInputStream fis = new FileInputStream(textFile);
        byte[] buffer = new byte[1024];
        int numRead;
        while ((numRead = fis.read(buffer)) != -1) {
            byteTmp.write(buffer, 0, numRead);
        }
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(byteTmp.toByteArray());
        byte[] hash = md5.digest();
        StringBuffer hexString = new StringBuffer();
        for (int j = 0; j < hash.length; j++) {
            String hexPart;
            hexPart = Integer.toHexString(0xFF & hash[j]);
            if (hexPart.length() == 1) {
                hexPart = "0" + hexPart;
            }
            hexString.append(hexPart);
        }
        String stringHash = hexString.toString();
        gzout.write(byteTmp.toByteArray());
        gzout.finish();
        gzout.close();
        char[] base = Base64.encode(byteOut.toByteArray());
        String base64String = new String(base);
        XmlRpcArray array = new XmlRpcArray();
        array.add(base64String);
        XmlRpcStruct responseStruct = ((XmlRpcStruct) client.invoke("DetectLanguage", new Object[] { token, array }));
        String status = responseStruct.getString("status");
        if (status.indexOf("20") != 0) {
            throw new OsdbException(status);
        }
        String lang = responseStruct.getStruct("data").getString(stringHash);
        return Language.xxxToxx(lang);
    }

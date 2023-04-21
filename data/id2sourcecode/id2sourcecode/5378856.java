    @Test
    public void base64() throws Exception {
        String testStr = "谢炳盛;mailxbs@126.com;beshineshe@qq.com;15910691199;";
        String base64Object = Base64.Object_Base64(testStr);
        Object Objectbase64 = Base64.Base64_Object(base64Object);
        System.out.println(testStr);
        System.out.println(base64Object);
        System.out.println(Objectbase64);
        assertEquals(testStr, Objectbase64);
        System.out.println("=======================================");
        System.out.println(MD5.digest(testStr));
        System.out.println("=======================================");
        Date d0 = new Date();
        KeyPair key = RSA.generateKeyPair(1024);
        Date d1 = new Date();
        String rsaStr = RSA.encryptBase64(key.getPublic(), testStr);
        Date d2 = new Date();
        String strRsa = RSA.decryptBase64(key.getPrivate(), rsaStr);
        Date d3 = new Date();
        System.out.println(rsaStr);
        System.out.println(strRsa);
        System.out.println(d1.getTime() - d0.getTime());
        System.out.println(d2.getTime() - d1.getTime());
        System.out.println(d3.getTime() - d2.getTime());
        d1 = new Date();
        rsaStr = RSA.encryptBase64(key.getPublic(), testStr);
        d2 = new Date();
        strRsa = RSA.decryptBase64(key.getPrivate(), rsaStr);
        d3 = new Date();
        System.out.println(d2.getTime() - d1.getTime());
        System.out.println(d3.getTime() - d2.getTime());
    }

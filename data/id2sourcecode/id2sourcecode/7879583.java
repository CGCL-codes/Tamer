    @Test
    public void testCheck() {
        EsapiDigester digester = new EsapiDigester(true, "esapi");
        String password1 = digester.digest("1234567890123456789012345678901234567890123456789012345678901234");
        System.out.println("length=" + password1.length());
        System.out.println("password1=" + password1);
        String password2 = digester.digest("1234567890123456789012345678901234567890123456789012345678901234");
        System.out.println("password2=" + password2);
        assertTrue(digester.check("1234567890123456789012345678901234567890123456789012345678901234", password1));
        assertTrue(digester.check("1234567890123456789012345678901234567890123456789012345678901234", password2));
        assertTrue(password1.equals(password2));
    }

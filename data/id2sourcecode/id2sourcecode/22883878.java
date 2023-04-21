    @Test
    public void testCheck() {
        JasyptDigester digester = new JasyptDigester();
        digester.setPasswordEncryptor(new StrongPasswordEncryptor());
        String password1 = digester.digest("josh");
        System.out.println("password1=" + password1);
        String password2 = digester.digest("josh");
        System.out.println("password2=" + password2);
        assertTrue(digester.check("josh", password1));
        assertTrue(digester.check("josh", password2));
        assertFalse(password1.equals(password2));
    }

    public void testCredential() {
        Credential[] creds = { new Password("Foo"), Credential.getCredential("Foo"), Credential.getCredential(Credential.Crypt.crypt("user", "Foo")), Credential.getCredential(Credential.MD5.digest("Foo")) };
        assertTrue("c[0].check(c[0])", creds[0].check(creds[0]));
        assertTrue("c[0].check(c[1])", creds[0].check(creds[1]));
        assertTrue("c[0].check(c[2])", creds[0].check(creds[2]));
        assertTrue("c[0].check(c[3])", creds[0].check(creds[3]));
        assertTrue("c[1].check(c[0])", creds[1].check(creds[0]));
        assertTrue("c[1].check(c[1])", creds[1].check(creds[1]));
        assertTrue("c[1].check(c[2])", creds[1].check(creds[2]));
        assertTrue("c[1].check(c[3])", creds[1].check(creds[3]));
        assertTrue("c[2].check(c[0])", creds[2].check(creds[0]));
        assertTrue("c[2].check(c[1])", creds[2].check(creds[1]));
        assertTrue("c[2].check(c[2])", !creds[2].check(creds[2]));
        assertTrue("c[2].check(c[3])", !creds[2].check(creds[3]));
        assertTrue("c[3].check(c[0])", creds[3].check(creds[0]));
        assertTrue("c[3].check(c[1])", creds[3].check(creds[1]));
        assertTrue("c[3].check(c[2])", !creds[3].check(creds[2]));
        assertTrue("c[3].check(c[3])", creds[3].check(creds[3]));
    }

    public void testUserImport() throws Exception {
        CmsObject cms = getCmsObject();
        echo("Testing the user import.");
        I_CmsPasswordHandler passwordHandler = new CmsDefaultPasswordHandler();
        CmsUser user;
        echo("Testing passwords of imported users");
        user = cms.readUser("Admin");
        assertEquals(user.getPassword(), passwordHandler.digest("admin", "MD5", CmsEncoder.ENCODING_UTF_8));
        user = cms.readUser("Guest");
        assertEquals(user.getPassword(), passwordHandler.digest("", "MD5", CmsEncoder.ENCODING_UTF_8));
        user = cms.readUser("test1");
        assertEquals(user.getPassword(), passwordHandler.digest("test1", "MD5", CmsEncoder.ENCODING_UTF_8));
        user = cms.readUser("test2");
        assertEquals(user.getPassword(), passwordHandler.digest("test2", "MD5", CmsEncoder.ENCODING_UTF_8));
    }

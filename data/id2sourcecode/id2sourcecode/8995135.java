    @Test
    public void testMD5WithPostgres() {
        Realm realm = new SimpleRealm();
        String md5expected = "038d2263a60439fb7821eb792e58e8e4";
        logger.info(realm.digest("22uCxYT"));
        Assert.assertEquals(md5expected, realm.digest("22uCxYT"));
    }

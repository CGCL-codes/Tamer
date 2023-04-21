    @Test
    public void testPathForSFTP() throws Exception {
        URL url = UrlFactory.getUrl("sftp://admin:admin@192.168.1.1/opt/hitachi/cnp/data/pm/reports/3gpp/5/data.xml");
        URLConnection conn = url.openConnection();
        Assert.assertTrue(conn instanceof SftpUrlConnection);
        UrlFactory.disconnect(conn);
    }

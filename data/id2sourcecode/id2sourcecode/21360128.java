    @Test
    public void testPathFor3GPPA() throws Exception {
        URL url = UrlFactory.getUrl("sftp.3gpp://admin:admin@192.168.1.1/opt/hitachi/cnp/data/pm/reports/3gpp/5?step=300&timezone=GMT-5&neId=MME00001");
        URLConnection conn = url.openConnection();
        Assert.assertTrue(conn instanceof Sftp3gppUrlConnection);
        String path = ((Sftp3gppUrlConnection) conn).getPath();
        log().debug(path);
        UrlFactory.disconnect(conn);
    }

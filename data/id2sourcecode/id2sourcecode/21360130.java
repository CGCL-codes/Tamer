    @Test
    public void testGetTimeStampFromFile() throws Exception {
        URL url = UrlFactory.getUrl("sftp.3gpp://admin:admin@192.168.1.1/opt/3gpp?step=300&neId=MME00001&deleteFile=true");
        URLConnection conn = url.openConnection();
        Assert.assertTrue(conn instanceof Sftp3gppUrlConnection);
        Sftp3gppUrlConnection c = (Sftp3gppUrlConnection) conn;
        Assert.assertTrue(Boolean.parseBoolean(c.getQueryMap().get("deletefile")));
        long t1 = c.getTimeStampFromFile("A20111102.1300-0500-1305-0500_MME00001");
        long t2 = c.getTimeStampFromFile("A20111102.1305-0500-1310-0500_MME00001");
        Assert.assertTrue(t2 > t1);
        Assert.assertTrue(t2 - t1 == Long.parseLong(c.getQueryMap().get("step")) * 1000);
    }

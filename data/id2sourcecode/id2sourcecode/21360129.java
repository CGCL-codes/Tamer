    @Test
    public void testCustomPathFor3GPPA() throws Exception {
        long ts = 1320257100000l;
        Date date = new Date(ts);
        log().debug("Timestamp = " + date);
        URL url = UrlFactory.getUrl("sftp.3gpp://admin:admin@192.168.1.1/opt/3gpp?step=300&timezone=GMT-5&neId=MME00001&referenceTimestamp=" + ts);
        URLConnection conn = url.openConnection();
        Assert.assertTrue(conn instanceof Sftp3gppUrlConnection);
        String path = ((Sftp3gppUrlConnection) conn).getPath();
        log().debug(path);
        UrlFactory.disconnect(conn);
        Assert.assertEquals("/opt/3gpp/A20111102.1300-0500-1305-0500_MME00001", path);
    }

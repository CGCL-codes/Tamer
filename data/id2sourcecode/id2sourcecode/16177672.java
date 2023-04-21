    public void testNoAclParser() throws Throwable {
        File rc = File.createTempFile("running-config", "");
        InputStream i = getClass().getResourceAsStream("running-config.cfg");
        if (i == null) throw new Exception("Invalid compilation, running-config not found in class folder");
        OutputStream f = new FileOutputStream(rc);
        byte[] buf = new byte[10000];
        while (i.read(buf) > 0) f.write(buf);
        f.close();
        String no = CiscoUtil.noAclParser(rc);
        assertNotNull("parser returned null", no);
        assertFalse("parser returned empty string", "".equals(no));
        System.out.println(no);
    }

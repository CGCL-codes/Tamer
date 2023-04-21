    public void testStartup() throws Exception {
        _service.startup();
        System.out.print("Waiting for startup ...");
        Thread.sleep(10 * 1000L);
        System.out.println("Done.");
        URL url = new URL("http://localhost:7788/index.html");
        URLConnection conn = url.openConnection();
        conn.setConnectTimeout(10 * 1000);
        InputStream in = null;
        try {
            StringBuffer strBuff = new StringBuffer();
            byte[] buff = new byte[1024];
            in = conn.getInputStream();
            int len = in.read(buff, 0, buff.length);
            while (len != -1) {
                strBuff.append(new String(buff, 0, len));
                len = in.read(buff, 0, buff.length);
            }
            assertNotNull("Check contents.", strBuff.toString().contains("test root document"));
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

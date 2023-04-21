    public void testCreateSequence() throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        for (int y = 0; y < 8; y++) {
            InputStream rfcIS = Thread.currentThread().getContextClassLoader().getResourceAsStream(ltansRFC);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int s = -1;
            while ((s = rfcIS.read()) != -1) baos.write(s);
            byte[] blockInputByte = new byte[1024];
            int inputStreamPos = 0;
            byte[] rfcBytes = baos.toByteArray();
            int nodeNum = (int) Math.pow((double) 2, (double) y);
        }
    }

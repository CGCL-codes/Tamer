    public static void main(String[] args) {
        try {
            IDfLoginInfo l = new DfLoginInfo();
            l.setUser(args[1]);
            l.setPassword(args[2]);
            IDfSession d = DfClient.getLocalClient().newSession(args[0], l);
            InputStream hIn = new DctmInputStream(d, "/Temp/TargetSetup.Result");
            ByteArrayOutputStream bTemp = new ByteArrayOutputStream();
            byte[] Bytes = new byte[4096];
            int nBytesRead = -1;
            while ((nBytesRead = hIn.read(Bytes)) != -1) bTemp.write(Bytes, 0, nBytesRead);
            System.out.println(new String(bTemp.toByteArray()));
            hIn.close();
        } catch (Exception exc) {
            exc.printStackTrace(System.err);
        }
    }

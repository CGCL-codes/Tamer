    public Response generateKeyPairs(String path, String userId, String pin) {
        String tempFilePath = Configuration.LOCAL_PRIVATE_RESOURCES_PATH + "/dhkeys.txt";
        try {
            String[] command = { Configuration.EXECUTABLES_PATH + "/wpes2_linux", "dh", pin, tempFilePath };
            Runtime.getRuntime().exec(command);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        LocalPrivateResource lpr = new LocalPrivateResource();
        try {
            lpr.set("UserInfo", "userId", userId);
            lpr.set("UserInfo", "pin", SecurityAlgorithms.encryptAES(pin));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String dhkeypairs = FileSystemUtils.readPrivateFile("dhkeys.txt");
        FileSystemUtils.deletePrivateFile("dhkeys.txt");
        return client.sendAndWait(ClientPrimitives.OE_DHKEYPAIRS, path, dhkeypairs, userId);
    }

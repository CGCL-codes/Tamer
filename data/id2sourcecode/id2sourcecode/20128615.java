    private void sendLogFileToWebServer() throws IOException, ATCommandFailedException {
        log("PreSendLog2");
        InputStream i = fileConnection.openInputStream();
        byte[] b = new byte[((int) fileConnection.fileSize())];
        String buff = new String();
        i.read(b);
        for (int idx = 0, idy = 0; idx < b.length; idx++) {
            buff = buff + ((char) b[idx]);
            if (idy == 200) {
                sendgrpsdata(ModuleResources.getInstance().getServerLogBase() + "&id=" + readModuleId() + "&log=" + ModuleResources.getInstance().getVersion() + Utilities.writeUTF(buff.toString()));
                idy = 0;
                buff = "";
            }
            idy++;
        }
        sendgrpsdata(ModuleResources.getInstance().getServerLogBase() + "&id=" + readModuleId() + "&log=" + ModuleResources.getInstance().getVersion() + Utilities.writeUTF(buff.toString()));
        fileConnection.delete();
        initializeLogFile();
    }

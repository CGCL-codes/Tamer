    public void putFile(GPESecurityManager secMgr, InputStream is, String remoteFile) throws Exception {
        GASSFileTransferClient transfer = (GASSFileTransferClient) storage.importFile(remoteFile, FileTransferClient.GASS, false);
        HTTPOutputStream os = transfer.getOutputStream();
        Thread.sleep(100);
        int chunk = 16384;
        byte[] buf = new byte[chunk];
        int read = 0;
        while (read != -1) {
            read = is.read(buf, 0, chunk);
            if (read != -1) {
                os.write(buf, 0, read);
            }
        }
        os.close();
        transfer.destroy();
    }

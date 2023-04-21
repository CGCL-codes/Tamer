    public void putFile(GPESecurityManager secMgr, InputStream is, String remoteFile) throws Exception {
        if (secMgr instanceof GSSSecurityManager) {
            GridFTPFileTransferClient client = (GridFTPFileTransferClient) storage.importFile(remoteFile, FileTransferClient.GRIDFTP, false);
            GridFTPOutputStream os = client.getOutputStream((GSSSecurityManager) secMgr);
            int chunk = 16384;
            byte[] buf = new byte[chunk];
            int read = 0;
            while (read != -1) {
                read = is.read(buf, 0, chunk);
                if (read != -1) {
                    os.write(buf, 0, read);
                }
            }
            client.destroy();
            os.close();
        } else {
            throw new TransferFailedException("File export failed");
        }
    }

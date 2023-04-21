    public void putFile(GPESecurityManager manager, InputStream is, String remoteFile) throws Exception {
        BaselineFileTransferClient transfer = (BaselineFileTransferClient) storage.importFile(remoteFile, FileTransferClient.BASELINE, false);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        int chunk = 16384;
        byte[] buf = new byte[chunk];
        int read = 0;
        while (read != -1) {
            read = is.read(buf, 0, chunk);
            if (read != -1) {
                bout.write(buf, 0, read);
            }
        }
        bout.close();
        transfer.putFile(bout.toByteArray());
    }

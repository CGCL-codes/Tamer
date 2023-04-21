    public void execute(IWorkerStatusController worker) throws Exception {
        IMailFolderCommandReference r = (IMailFolderCommandReference) getReference();
        Object[] uids = r.getUids();
        IMailbox folder = (IMailbox) r.getSourceFolder();
        ((StatusObservableImpl) folder.getObservable()).setWorker(worker);
        Object uid = uids[0];
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(folder.getMessageSourceStream(uid));
            tempFile = TempFileStore.createTempFileWithSuffix("txt");
            out = new BufferedOutputStream(new FileOutputStream(tempFile));
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer, 0, buffer.length)) > 0) {
                out.write(buffer, 0, read);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ioe) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ioe) {
                }
            }
        }
    }

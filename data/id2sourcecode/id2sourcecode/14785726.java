    private void transferFile(InputStream in, OutputStream out, FileTransferProgress progress) throws IOException, TransferCancelledException {
        try {
            long bytesSoFar = 0;
            byte[] buffer = new byte[BLOCKSIZE];
            int read;
            while ((read = in.read(buffer)) > -1) {
                if ((progress != null) && progress.isCancelled()) {
                    throw new TransferCancelledException();
                }
                if (read > 0) {
                    out.write(buffer, 0, read);
                    bytesSoFar += read;
                    if (progress != null) {
                        progress.progressed(bytesSoFar);
                    }
                }
            }
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException ex) {
            }
        }
    }

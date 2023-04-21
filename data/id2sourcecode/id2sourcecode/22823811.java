    void uploadFile(OutputStream outputStream, long amount) throws JUploadException, JUploadInterrupted {
        this.uploadPolicy.displayDebug("in UploadFileData.uploadFile (amount:" + amount + ", getUploadLength(): " + getUploadLength() + ")", 30);
        InputStream inputStream = getInputStream();
        while (amount > 0 && !this.fileUploadManagerThread.isUploadFinished()) {
            if (Thread.interrupted()) {
                throw new JUploadInterrupted(getClass().getName() + ".uploadFile [" + this.getFileName() + "]", this.uploadPolicy);
            }
            int toread = (amount > BUFLEN) ? BUFLEN : (int) amount;
            int towrite = 0;
            try {
                towrite = inputStream.read(this.readBuffer, 0, toread);
            } catch (IOException e) {
                throw new JUploadIOException(e);
            }
            if (towrite > 0) {
                try {
                    outputStream.write(this.readBuffer, 0, towrite);
                    this.fileUploadManagerThread.nbBytesUploaded(towrite, this);
                    amount -= towrite;
                    this.uploadRemainingLength -= towrite;
                    if (this.uploadPolicy.getDebugLevel() > 100) {
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                        }
                    }
                } catch (IOException ioe) {
                    throw new JUploadIOException(this.getClass().getName() + ".uploadFile()", ioe);
                } catch (Exception e) {
                    throw new JUploadException(this.getClass().getName() + ".uploadFile()  (check the user permission on the server)", e);
                }
            }
        }
    }

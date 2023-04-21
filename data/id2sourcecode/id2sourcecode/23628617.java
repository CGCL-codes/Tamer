    void retrieveFile() throws IOException, InterruptedException {
        s.setSoTimeout(1000);
        startTime = System.currentTimeMillis();
        if (curSize != 0) {
            curBytes += curSize;
        }
        Debug.log(Debug.DOWNLOAD, "Total size = " + totalBytes + ", cur size = " + curBytes);
        dr.setStatus(DownloadRequest.IN_TRANSIT);
        while (curBytes < totalBytes) {
            if (isInterrupted()) {
                dr.setStatus(DownloadRequest.CANCELLED);
                msg = "Interrupted.";
                return;
            }
            int amount = 0;
            try {
                amount = bis.read(readBuf);
            } catch (InterruptedIOException iie) {
                Debug.log(Debug.DOWNLOAD_CONTROL, "io interrupt (read)");
                if (isInterrupted()) {
                    Debug.log(Debug.DOWNLOAD_CONTROL, "thread was interrupted");
                    throw new InterruptedException();
                }
                amount = iie.bytesTransferred;
                if (amount > 0) {
                    curBytes += amount;
                    raf.write(readBuf, 0, amount);
                }
                if (curBytes + BYTE_ERROR_MARGIN > totalBytes) {
                    Debug.log(Debug.DOWNLOAD_CONTROL, "download complete anyway");
                    break;
                }
                continue;
            } catch (SocketException ex) {
                if (isInterrupted_WORKAROUND_FOR_BUG_IN_IBM_JDK) {
                    Debug.log(Debug.DOWNLOAD_CONTROL, "IBM-JDK: thread was interrupted");
                    throw new InterruptedException();
                }
                if (curBytes + BYTE_ERROR_MARGIN > totalBytes) {
                    break;
                }
                throw ex;
            } catch (IOException iie) {
                if (curBytes + BYTE_ERROR_MARGIN > totalBytes) {
                    break;
                }
                throw iie;
            } catch (Exception se) {
                if (curBytes + BYTE_ERROR_MARGIN > totalBytes) {
                    break;
                }
                throw new IOException(se.getMessage());
            }
            if (amount > 0) {
                curBytes += amount;
                try {
                    raf.write(readBuf, 0, amount);
                } catch (InterruptedIOException iie) {
                    Debug.log(Debug.DOWNLOAD_CONTROL, "io interrupt (write)");
                    if (isInterrupted()) {
                        Debug.log(Debug.DOWNLOAD_CONTROL, "thread was interrupted");
                        throw new InterruptedException();
                    }
                }
            } else if (amount < 0) {
                if (curBytes + BYTE_ERROR_MARGIN > totalBytes) {
                    break;
                }
                throw new EOFException("Premature end of data");
            }
        }
        dr.setStatus(DownloadRequest.COMPLETE);
        msg = "Download complete.";
    }

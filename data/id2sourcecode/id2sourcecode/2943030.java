        private synchronized void fileData(byte[] blob, int read, final long bytesLeft) {
            if (currentFileStream != null) {
                try {
                    if (connectionState == CONNSTATE_FILE_BODY) {
                        currentFileStream.write(blob, 0, read);
                        currentFileStream.flush();
                        if (bytesLeft < 1) {
                            connectionState = CONNSTATE_FILE_HEADER;
                            final String filename = currentFileStream.file.getAbsolutePath();
                            currentFileStream.close();
                            currentFileStream = null;
                            service.log(currentFileName + " got");
                            sendNotification(message.messageId, filename, currentFileSize, currentFileSize - bytesLeft, true, null, participantUid);
                        } else {
                            sendNotification(message.messageId, currentFileStream.getFile().getAbsolutePath(), currentFileSize, currentFileSize - bytesLeft, true, null, participantUid);
                        }
                    }
                } catch (IOException e) {
                    ServiceUtils.log(e);
                    try {
                        currentFileStream.close();
                    } catch (IOException e1) {
                        ServiceUtils.log(e);
                    }
                    currentFileStream = null;
                }
            }
        }

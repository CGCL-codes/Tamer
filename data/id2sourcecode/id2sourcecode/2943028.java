        private void sendFileToSocket(final File file, long startFrom) {
            OutputStream os;
            try {
                os = socket.getOutputStream();
            } catch (IOException e) {
                service.log(e);
                transferFailed(e, file.getAbsolutePath(), message, participantUid);
                cleanup();
                return;
            }
            long length = file.length();
            if (length > 8000) {
                buffer = new byte[8000];
            } else {
                buffer = new byte[(int) length];
            }
            currentFileSizeLeft = 0;
            int read = 0;
            service.log("sending " + file.getName() + " to " + participantUid);
            FileInputStream fis;
            try {
                fis = new FileInputStream(file);
                if (startFrom > 0) {
                    fis.skip(startFrom);
                    currentFileSizeLeft += startFrom;
                }
                BufferedInputStream bis = new BufferedInputStream(fis, 8000);
                while (currentFileSizeLeft < length) {
                    read = bis.read(buffer, 0, buffer.length);
                    if (read < 0) {
                        break;
                    }
                    os.write(buffer, 0, read);
                    os.flush();
                    currentFileSizeLeft += read;
                    service.log("sent " + currentFileSizeLeft + " bytes");
                    sendNotification(message.messageId, file.getAbsolutePath(), length, currentFileSizeLeft, false, null, participantUid);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        service.log(e);
                    }
                }
            } catch (IOException e) {
                service.log(e);
                transferFailed(e, file.getAbsolutePath(), message, participantUid);
                cleanup();
                return;
            }
            connectionState = CONNSTATE_FILE_HEADER;
            service.log(file.getName() + " sent");
        }

    public synchronized Result sendFile(File file) {
        activityStatus = FilesActivity.UPLOADING;
        cancelled = false;
        changeProgress(0);
        if (file.exists()) {
            String fileName = file.getName();
            long fileSize = file.length();
            try {
                dataOut.writeInt(UPLOAD_SOURCE_FILE_COMMAND);
                dataOut.writeInt(fileName.length());
                dataOut.writeBytes(fileName);
                if (dataIn.readInt() == BAD_PATH_ERROR) {
                    activityStatus = FilesActivity.IDLE;
                    return Result.FAILED;
                }
                dataOut.writeLong(file.lastModified());
                BufferedInputStream fileIn = new BufferedInputStream(new FileInputStream(file));
                byte[] fileBytes;
                int chunkToWrite;
                int read;
                final int MAX_CHUNK = DATA_CHUNK_SIZE;
                for (long bytesLeft = fileSize; bytesLeft > 0; bytesLeft -= MAX_CHUNK) {
                    changeProgress(100 - (int) ((100 * bytesLeft) / fileSize));
                    chunkToWrite = (int) ((bytesLeft > MAX_CHUNK) ? MAX_CHUNK : bytesLeft);
                    dataOut.writeLong(chunkToWrite);
                    fileBytes = new byte[chunkToWrite];
                    read = fileIn.read(fileBytes);
                    while ((read != -1) && (chunkToWrite > 0)) {
                        dataOut.write(fileBytes, 0, read);
                        chunkToWrite -= read;
                        fileBytes = new byte[chunkToWrite];
                        read = fileIn.read(fileBytes);
                    }
                    if (cancelled) break;
                }
                fileIn.close();
                if (cancelled) {
                    dataOut.writeInt(CANCEL_TRANSFER_COMMAND);
                    changeProgress(0);
                } else {
                    dataOut.writeInt(END_OF_DATA_RESPONSE);
                    changeProgress(100);
                }
                int response = dataIn.readInt();
                activityStatus = FilesActivity.IDLE;
                if (response == OK_RESPONSE) return Result.OK; else if (response == TRANSFER_CANCELLED_RESPONSE) return Result.CANCELLED;
            } catch (IOException e) {
                changeProgress(0);
                parent.disconnectFromFiles();
            }
        }
        activityStatus = FilesActivity.IDLE;
        return Result.FAILED;
    }

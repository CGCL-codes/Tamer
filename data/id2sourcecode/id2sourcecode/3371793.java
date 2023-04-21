    private void downloadFileCommand() throws IOException {
        int dataLength = dataIn.readInt();
        byte[] bytes = new byte[dataLength];
        dataIn.readFully(bytes);
        String fileName = new String(bytes);
        if ((fileName.indexOf("..") != -1) || (fileName.indexOf(":") != -1) || fileName.startsWith("/") || fileName.startsWith("\\")) {
            dataOut.writeInt(BAD_PATH_ERROR);
            return;
        }
        File file = new File(OUTPUT_PATH + File.separator + fileName);
        if (!file.exists() || file.isDirectory()) {
            dataOut.writeInt(FILE_NOT_FOUND_ERROR);
            return;
        }
        BufferedInputStream fileIn;
        try {
            fileIn = new BufferedInputStream(new FileInputStream(file));
        } catch (Exception e) {
            dataOut.writeInt(FILE_READ_ERROR);
            return;
        }
        dataOut.writeInt(OK_RESPONSE);
        long fileSize = file.length();
        dataOut.writeLong(fileSize);
        dataOut.writeLong(file.lastModified());
        String hash = hashFile(file);
        dataOut.writeInt(hash.length());
        dataOut.writeBytes(hash);
        boolean cancelled = false;
        byte[] fileBytes;
        int chunkToWrite;
        int read;
        final int MAX_CHUNK = DATA_CHUNK_SIZE;
        for (long bytesLeft = fileSize; bytesLeft > 0; bytesLeft -= MAX_CHUNK) {
            chunkToWrite = (int) ((bytesLeft > MAX_CHUNK) ? MAX_CHUNK : bytesLeft);
            dataOut.writeInt(chunkToWrite);
            fileBytes = new byte[chunkToWrite];
            read = fileIn.read(fileBytes);
            while ((read != -1) && (chunkToWrite > 0)) {
                dataOut.write(fileBytes, 0, read);
                chunkToWrite -= read;
                fileBytes = new byte[chunkToWrite];
                read = fileIn.read(fileBytes);
            }
            if (dataIn.readInt() == CANCEL_TRANSFER_COMMAND) {
                cancelled = true;
                break;
            }
        }
        if (!cancelled) {
            dataOut.writeInt(END_OF_DATA_RESPONSE);
            dataOut.writeInt(OK_RESPONSE);
        } else {
            dataOut.writeInt(TRANSFER_CANCELLED_RESPONSE);
        }
        fileIn.close();
    }

    private void uploadFile(UploadFile file) throws UploadException, IOException {
        curUploadFileName = file.getFileName();
        uploadCurFileBytes = file.getLength();
        uploadCurFileBytesDone = 0;
        writeHTTPFileEntityHeader(file);
        FileInputStream in = new FileInputStream(file.getFile());
        byte buffer[] = new byte[FILE_BUFFER_SIZE];
        int bytes_read;
        do {
            bytes_read = in.read(buffer);
            if (bytes_read != -1) {
                outputStream.write(buffer, 0, bytes_read);
                uploadCurFileBytesDone += bytes_read;
                uploadSumBytesDone += bytes_read;
            }
        } while (bytes_read == FILE_BUFFER_SIZE);
        writeASCIIToOutputStream("\r\n");
    }

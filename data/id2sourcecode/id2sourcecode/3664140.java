    private Resource parseAndCreateResource(RequestHeader header, InputStream inputStream) throws IOException {
        String message = null;
        final boolean uploadEnabledSnapshot = isUploadEnabled();
        if (header.getMethod() == Method.POST) {
            if (uploadEnabledSnapshot) {
                String contentType = header.getField("Content-Type");
                String multiPartPrefix = "multipart/form-data; boundary=";
                if (contentType.startsWith(multiPartPrefix)) {
                    int fileCounter = 0;
                    String boundaryString = contentType.substring(multiPartPrefix.length());
                    byte[] boundary = boundaryString.getBytes("ASCII");
                    MultiPartFormDataReader multiPartReader = new MultiPartFormDataReader(inputStream, boundary);
                    Part part = multiPartReader.next();
                    byte[] buffer = new byte[1024];
                    while (part != null) {
                        fileCounter++;
                        String suggestedName = part.getFileName();
                        File file = filesUploadedByOthers.createFile(suggestedName);
                        FileOutputStream outputStream = new FileOutputStream(file);
                        InputStream partInputStream = part.getInputStream();
                        try {
                            int readedBytes = partInputStream.read(buffer);
                            while (readedBytes != -1) {
                                outputStream.write(buffer, 0, readedBytes);
                                readedBytes = partInputStream.read(buffer);
                            }
                        } finally {
                            outputStream.close();
                        }
                        synchronized (filesUploadedByOthers) {
                            filesUploadedByOthers.addEntry(suggestedName, file.getName());
                        }
                        part = multiPartReader.next();
                    }
                    if (fileCounter == 1) {
                        message = "Uploaded 1 file.";
                    } else {
                        message = String.format("Uploaded %d files.", fileCounter);
                    }
                }
            }
        }
        return createResource(header, message, uploadEnabledSnapshot);
    }

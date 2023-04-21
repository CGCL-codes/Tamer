    private void handleUploadPart(HttpConnection connection) throws IOException {
        OutputStream out = null;
        InputStream source = null;
        int sizeOut = Integer.MAX_VALUE;
        try {
            if (currentlyDownloading.sendData != null) {
                connection.setRequestMethod(HttpConnection.POST);
                sizeOut = currentlyDownloading.sendData.length;
                connection.setRequestProperty("Content-Length", Integer.toString(sizeOut));
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                out = connection.openOutputStream();
                source = new ByteArrayInputStream(currentlyDownloading.sendData);
            } else if (currentlyDownloading.sendFile != null) {
                connection.setRequestMethod(HttpConnection.POST);
                connection.setRequestProperty("Content-Type", "image/jpeg");
                FileConnection fileConnection = (FileConnection) Connector.open(currentlyDownloading.sendFile, Connector.READ);
                InputStream fileContent = fileConnection.openInputStream();
                sizeOut = (int) fileConnection.fileSize();
                String fileNameOnly = currentlyDownloading.sendFile.substring(currentlyDownloading.sendFile.lastIndexOf('/') + 1);
                connection.setRequestProperty("Content-Length", Integer.toString(sizeOut));
                connection.setRequestProperty("Filename", fileNameOnly);
                out = connection.openOutputStream();
                source = fileContent;
            }
            int updateChunks = calculateOptimumUpdateChunks(sizeOut);
            int read = 0, total = 0;
            byte[] chopper = new byte[updateChunks];
            while ((read = source.read(chopper)) != -1) {
                if (emergencyBrake) {
                    out.close();
                    break;
                }
                out.write(chopper, 0, read);
                total += read;
                currentlyDownloading.queueItemStatus((int) 100 * total / sizeOut);
                System.out.print(".");
            }
        } catch (IOException e) {
            System.err.println("Error while sending data from the Queue");
            throw e;
        } catch (SecurityException e) {
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException unhandled) {
                }
            }
        }
    }

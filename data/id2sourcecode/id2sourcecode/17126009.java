    private void downloadToFile(InputStream input, ByteArrayOutputStream downloadPart) {
        log("downloadToFile - start");
        try {
            fileData = File.createTempFile("httpResponse-cache-", "");
            fileData.deleteOnExit();
            byte[] buff = new byte[getBlockSize()];
            dataSize = 0;
            FileOutputStream fout = new FileOutputStream(fileData);
            if (downloadPart != null) {
                byte[] downloadPartData = downloadPart.toByteArray();
                fout.write(downloadPartData);
                dataSize += downloadPartData.length;
                log("copied to file =" + fileData.getName() + " dataSize=" + dataSize);
            }
            while (true) {
                int readed = input.read(buff);
                if (readed < 0) break;
                if (readed > 0) {
                    fout.write(buff, 0, readed);
                    dataSize += readed;
                }
                log("readed=" + readed + " dataSize=" + dataSize);
                fireProgress(this, request, this, dataSize);
            }
            fout.close();
        } catch (IOException ex) {
            variant = new NotAvaliabble();
            log("variant - NotAvaliabble");
            memoryData = null;
            fileData = null;
            Logger.getLogger(HttpResponse.class.getName()).log(Level.SEVERE, null, ex);
            log("downloadToFile - end");
            return;
        }
        variant = new FileData();
        log("variant - FileData");
        memoryData = null;
        log("downloadToFile - end");
    }

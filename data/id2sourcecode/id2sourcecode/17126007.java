    private void downloadToMemory(InputStream input) {
        log("downloadToMemory - start");
        ByteArrayOutputStream mem = new ByteArrayOutputStream();
        dataSize = 0;
        byte[] buff = new byte[getBlockSize()];
        try {
            while (true) {
                int readed = input.read(buff);
                if (readed < 0) break;
                if (readed > 0) {
                    mem.write(buff, 0, readed);
                    dataSize += readed;
                }
                log("readed=" + readed + " dataSize=" + dataSize);
                fireProgress(this, request, this, dataSize);
                long maxMemSize = getMaxMemorySize();
                if (dataSize > maxMemSize && maxMemSize >= 0) {
                    downloadToFile(input, mem);
                    log("downloadToMemory - end");
                    return;
                }
            }
        } catch (IOException ex) {
            variant = new NotAvaliabble();
            log("variant - NotAvaliabble");
            Logger.getLogger(HttpResponse.class.getName()).log(Level.SEVERE, null, ex);
            log("downloadToMemory - end");
            return;
        }
        memoryData = mem.toByteArray();
        variant = new MemoryData();
        log("variant - MemoryData");
        try {
            mem.close();
        } catch (IOException ex) {
            Logger.getLogger(HttpResponse.class.getName()).log(Level.SEVERE, null, ex);
        }
        log("downloadToMemory - end");
    }

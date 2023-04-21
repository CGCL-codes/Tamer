    public void run() {
        this.finished = false;
        byte[] buffer = new byte[this.bufferSize];
        int readCount = 0;
        OutputStream[] os;
        try {
            this.checkUsable();
            this.startConsumerThreads();
            os = (OutputStream[]) this.sinks.toArray(new OutputStream[this.sinks.size()]);
            while (true) {
                if ((readCount = this.originalStream.read(buffer)) != -1) {
                    for (int i = 0; i < os.length; i++) {
                        os[i].write(buffer, 0, readCount);
                    }
                    this.byteCount += readCount;
                } else {
                    for (int i = 0; i < os.length; i++) {
                        os[i].flush();
                        os[i].close();
                    }
                    break;
                }
            }
        } catch (IOException ioe) {
            log.error(ioe);
        } finally {
            try {
                if (this.originalStream != null) {
                    this.originalStream.close();
                }
            } catch (IOException ioe) {
            }
        }
        this.usable = false;
        this.finished = true;
    }

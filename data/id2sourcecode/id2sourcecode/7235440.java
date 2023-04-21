    public void run() {
        checkUsable();
        byte[] buffer = new byte[bufferSize];
        int readCount;
        byteCount = 0;
        usable = false;
        try {
            while (true) {
                if ((readCount = input.read(buffer)) != -1) {
                    output.write(buffer, 0, readCount);
                    byteCount += readCount;
                } else {
                    output.flush();
                    output.close();
                    break;
                }
            }
        } catch (IOException ioe) {
            log.error(ioe);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ioe) {
            }
        }
        finished = true;
    }

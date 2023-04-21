    public void run() {
        try {
            while (true) {
                int read = is.read(buffer);
                if (read == -1) break;
                os.write(buffer, 0, read);
            }
        } catch (IOException e) {
            log.debug(e.getMessage(), e);
        }
        close();
    }

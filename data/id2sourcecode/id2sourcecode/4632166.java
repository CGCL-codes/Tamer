    public void run() {
        try {
            byte[] b = new byte[512];
            int read = 1;
            while (read > -1) {
                read = input.read(b, 0, b.length);
                if (read > -1) {
                    output.write(b, 0, read);
                }
            }
        } catch (Exception e) {
            log.error(e);
        } finally {
            try {
                input.close();
            } catch (Exception e) {
                log.error(e);
            }
            try {
                output.close();
            } catch (Exception e) {
                log.error(e);
            }
        }
    }

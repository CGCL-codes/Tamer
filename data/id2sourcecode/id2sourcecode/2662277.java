    public ByteArrayOutputStream getOutputStream() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = this.getInputStream();
        if (in == null) return null;
        try {
            int read = -1;
            byte[] buff = new byte[256];
            while ((read = in.read(buff)) != -1) {
                out.write(buff, 0, read);
            }
            in.close();
        } catch (IOException e) {
            log.warn("Failed extracting response");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.warn("Failed closing stream");
                }
            }
        }
        return out;
    }

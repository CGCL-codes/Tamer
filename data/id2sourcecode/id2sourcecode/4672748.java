    private void sendBinay(String requestedFile, ServletOutputStream out) throws IOException {
        InputStream in = null;
        try {
            in = this.getClass().getResourceAsStream("/pool/" + requestedFile);
            if (in == null) {
                Logger.getLogger(this.getClass()).error("requested File does not exist: " + requestedFile);
                return;
            }
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) out.write(buffer, 0, bytesRead);
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                ;
            }
        }
    }

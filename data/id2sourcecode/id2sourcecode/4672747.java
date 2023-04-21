    private boolean send(String requestedFile, PrintWriter out) throws IOException {
        Logger.getLogger(this.getClass()).debug("Sending File: " + requestedFile);
        InputStreamReader in = null;
        try {
            InputStream inputStream = this.getClass().getResourceAsStream("/pool/" + requestedFile);
            if (inputStream == null) {
                Logger.getLogger(this.getClass()).error("requested File does not exist: " + requestedFile);
                return false;
            }
            in = new InputStreamReader(inputStream);
            char[] buffer = new char[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) out.write(buffer, 0, bytesRead);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    ;
                }
            }
        }
        return true;
    }

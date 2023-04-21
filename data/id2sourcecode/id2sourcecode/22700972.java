    public synchronized OutputStream writeNotify() throws IOException {
        if (conn != null) {
            throw new IOException("attempt to write on HttpSendSocket after " + "request has been sent");
        }
        conn = url.openConnection();
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestProperty("Content-type", "application/octet-stream");
        inNotifier.deactivate();
        in = null;
        return out = conn.getOutputStream();
    }

    public synchronized void append(ServerState state, String detail) throws Exception {
        ServerStatusMessage message = new ServerStatusMessage(state, null, detail);
        FileOutputStream out = null;
        FileChannel channel = null;
        FileLock lock = null;
        try {
            out = new FileOutputStream(_file, true);
            channel = out.getChannel();
            lock = channel.lock();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));
            serialize(message, writer);
            writer.close();
        } catch (IOException ioe) {
            throw new Exception("Error opening server status file for writing: " + _file.getPath(), ioe);
        } finally {
            if (lock != null) {
                try {
                    lock.release();
                } catch (Exception e) {
                }
            }
            if (channel != null) {
                try {
                    channel.close();
                } catch (Exception e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }

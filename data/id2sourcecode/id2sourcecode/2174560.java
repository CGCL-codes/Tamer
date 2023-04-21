    private void pipe(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer, 0, buffer.length)) != -1) {
            out.write(buffer, 0, read);
            out.flush();
            if (toTask != null) {
                synchronized (this) {
                    toTask.cancel();
                    toTimer.schedule(toTask, timeout);
                }
            }
        }
    }

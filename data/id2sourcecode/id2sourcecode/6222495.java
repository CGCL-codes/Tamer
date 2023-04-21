    private synchronized void pipe(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[1024];
        int nread;
        bytesSent = 0;
        isCanceled = false;
        synchronized (in) {
            while ((nread = in.read(buf, 0, buf.length)) >= 0) {
                out.write(buf, 0, nread);
                bytesSent += nread;
                if (isCanceled) {
                    throw new IOException("Canceled");
                }
                out.flush();
                this.setChanged();
                this.notifyObservers(bytesSent);
                this.clearChanged();
            }
        }
        out.flush();
        buf = null;
    }

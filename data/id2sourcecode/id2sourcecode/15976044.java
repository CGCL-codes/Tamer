    protected void fireWriteSelect() {
        synchronized (this) {
            if (write_listener != null && !write_selects_paused) {
                if (failed != null) {
                    write_selects_paused = true;
                    selector.ready(this, write_listener, write_attachment, failed);
                } else if (closed) {
                    write_selects_paused = true;
                    selector.ready(this, write_listener, write_attachment, new Throwable("Transport closed"));
                } else if (connection.canWrite()) {
                    write_selects_paused = true;
                    selector.ready(this, write_listener, write_attachment);
                }
            }
        }
    }

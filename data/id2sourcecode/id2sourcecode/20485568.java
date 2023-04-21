    public void run() {
        try {
            while (activate) {
                synchronized (this) {
                    if (buffer != null) {
                        String msg = (String) new IFXPackager().pack(buffer);
                        out.write(msg.getBytes());
                        buffer = null;
                    }
                }
            }
        } catch (IOException e) {
            logger.error(connector.getChannelName() + "| Problema de escritura, " + e.getMessage() + "|");
        }
        notify();
    }

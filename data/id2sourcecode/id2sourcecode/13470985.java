    public void close() {
        synchronized (lock) {
            try {
                if (istream != null) {
                    istream.getChannel().close();
                    istream.close();
                    if (parseBuffer != null) {
                        log.debug("releasing parse memory map");
                        clean(parseBuffer);
                    }
                    parseBuffer = null;
                }
            } catch (IOException ex) {
                throw new HermesRuntimeException(ex);
            }
        }
    }

        public void run() {
            try {
                RPServiceContext.initialize(socket);
                handler.handleRequest(request, reader, writer);
            } catch (IOException ioe) {
                log.error("IOError: " + ioe, ioe);
            } catch (Throwable t) {
                log.error("Error", t);
            } finally {
                RPServiceContext.dispose();
                try {
                    writer.flush();
                    socket.close();
                } catch (IOException ioe) {
                    log.error("Error closing socket", ioe);
                }
            }
        }

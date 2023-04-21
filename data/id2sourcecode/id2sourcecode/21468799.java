        @Override
        public void run() {
            try {
                while (!to.isClosed() && !from.isClosed()) {
                    to.getOutputStream().write(from.getInputStream().read());
                }
            } catch (IOException e) {
            } finally {
                if (!from.isClosed()) {
                    try {
                        from.close();
                    } catch (IOException e1) {
                        LogFactory.getLog(Forwarder.class).error(e1, e1);
                    }
                }
                if (!to.isClosed()) {
                    try {
                        to.close();
                    } catch (IOException e1) {
                        LogFactory.getLog(Forwarder.class).error(e1, e1);
                    }
                }
            }
        }

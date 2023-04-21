        @Override
        public void run() {
            try {
                while (true) {
                    to.getOutputStream().write(from.getInputStream().read());
                }
            } catch (IOException e) {
                if (from.isConnected()) {
                    try {
                        from.close();
                    } catch (IOException e1) {
                    }
                }
                if (to.isConnected()) {
                    try {
                        to.close();
                    } catch (IOException e1) {
                    }
                }
            }
        }

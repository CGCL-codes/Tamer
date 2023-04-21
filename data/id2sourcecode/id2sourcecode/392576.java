        public void run() {
            try {
                doEAP(socket, reader, writer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

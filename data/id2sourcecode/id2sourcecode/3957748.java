        public void run() {
            try {
                byte[] cbuf = new byte[2048];
                int read = -1;
                while ((read = in.read(cbuf)) > -1) {
                    getConsole().getOut().write(cbuf, 0, read);
                    getConsole().getOut().flush();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

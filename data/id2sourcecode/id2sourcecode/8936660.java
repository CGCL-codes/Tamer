        public void run() {
            try {
                org.softnetwork.log.Log4jConnector.getConsole().debug("running");
                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) >= 0) out.write(buffer, 0, len);
                in.close();
                out.close();
            } catch (IOException io) {
            }
        }

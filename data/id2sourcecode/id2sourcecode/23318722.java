        @Override
        public void run() {
            active = true;
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buffer = new byte[2048];
                int readBytes;
                while (active && (readBytes = in.read(buffer)) != -1) {
                    out.write(buffer, 0, readBytes);
                }
                buf = out.toByteArray();
            } catch (IOException e) {
                cause = e;
            }
        }

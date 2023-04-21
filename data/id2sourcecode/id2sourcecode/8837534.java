        private void writeClass(String className) throws IOException {
            final InputStream classIn = ClassLoader.getSystemResourceAsStream(className);
            final OutputStream out = socket.getOutputStream();
            if (classIn != null) {
                try {
                    final byte[] buffer = new byte[2048];
                    int read;
                    while ((read = classIn.read(buffer)) != -1) {
                        out.write(buffer, 0, read);
                    }
                } finally {
                    classIn.close();
                }
            } else {
                out.write(NOT_FOUND_404);
            }
            out.flush();
        }

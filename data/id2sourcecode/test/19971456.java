        private void postTest(int length, InputStream in) {
            try {
                ByteArrayOutputStream data = new ByteArrayOutputStream();
                for (int i = 0; i < length; i++) {
                    data.write(in.read());
                }
                if (length == -1) {
                    int len = in.read() - 48;
                    in.read();
                    in.read();
                    while (len > 0) {
                        for (int i = 0; i < len; i++) {
                            data.write(in.read());
                        }
                        in.read();
                        in.read();
                        len = in.read() - 48;
                        in.read();
                        in.read();
                    }
                    in.read();
                    in.read();
                }
                OutputStream os = socket.getOutputStream();
                print(os, "HTTP/1.1 " + OK + " OK\r\n");
                print(os, "Content-Length: " + data.size() + "\r\n\r\n");
                os.write(data.toByteArray());
                os.flush();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

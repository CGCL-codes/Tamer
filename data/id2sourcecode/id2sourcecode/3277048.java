            @Override
            public void close() throws IOException {
                if (isClosed) return;
                isClosed = true;
                os.flush();
                out.write("\r\n");
                out.write("--");
                out.write(BOUNDARY);
                out.write("--");
                out.write("\r\n");
                out.flush();
                out.close();
                java.io.InputStream in = conn.getInputStream();
                int read = in.read();
                while (read >= 0) {
                    if (receiver != null) receiver.write(read);
                    read = in.read();
                }
                in.close();
            }

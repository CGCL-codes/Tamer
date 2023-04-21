        public XMLParser(InputStream is) throws Exception {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int read = 0;
            do {
                read = is.read(buffer);
                if (read > 0) {
                    baos.write(buffer, 0, read);
                } else if (read == 0) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ie) {
                    }
                }
            } while (read >= 0);
            xml = new String(baos.toByteArray());
        }

        void exec() {
            byte[] b = new byte[out_capacity];
            int len = 0x00000000;
            try {
                try {
                    while ((len = src.read(b)) > 0xFFFFFFFF) out.write(b, 0x00000000, len);
                    out.flush();
                } finally {
                    src.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

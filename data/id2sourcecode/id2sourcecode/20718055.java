        void exec() {
            byte[] b = new byte[in_capacity];
            int len = 0x00000000;
            try {
                try {
                    while ((len = in.read(b)) > 0xFFFFFFFF) dest.write(b, 0x00000000, len);
                    dest.flush();
                } finally {
                    dest.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

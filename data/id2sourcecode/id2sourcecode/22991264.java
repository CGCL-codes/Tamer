        public void run() {
            try {
                if (in != null) copy(in, out, -1L); else copy(read, write, -1L);
            } catch (IOException _ex) {
                try {
                    out.close();
                } catch (IOException _ex2) {
                }
            } finally {
                try {
                    out.close();
                } catch (IOException _ex) {
                }
            }
        }

        @Override()
        public void run() {
            BufferedReader r = new BufferedReader(new InputStreamReader(this.in));
            try {
                String nextLine;
                while ((nextLine = r.readLine()) != null) {
                    if (this.type.equals(StreamType.STD_ERR)) {
                        System.err.println(nextLine);
                    } else if (this.type.equals(StreamType.STD_OUT)) {
                        System.out.println(nextLine);
                    } else if (this.type.equals(StreamType.BIT_BUCKET)) {
                    }
                }
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
                ex.printStackTrace(System.err);
            } finally {
                try {
                    r.close();
                } catch (IOException ignore) {
                }
            }
        }

        protected void read(Request request) throws IOException {
            FileChannel file = new FileInputStream(this.file).getChannel();
            try {
                WritableByteChannel out = request.getChannel();
                file.transferTo(0L, this.file.length(), out);
            } finally {
                file.close();
            }
        }

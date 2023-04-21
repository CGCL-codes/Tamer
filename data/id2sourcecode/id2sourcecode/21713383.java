        public boolean process(Reader reader, Writer writer) throws IOException {
            int c = -1;
            while ((c = reader.read()) != -1) {
                writer.write(c);
            }
            return true;
        }

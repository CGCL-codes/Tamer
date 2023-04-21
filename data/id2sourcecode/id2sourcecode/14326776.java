        public IntraFile(String sourceName, Reader reader, Writer writer) {
            this.sourceName = sourceName;
            this.in = new LineNumberReader(reader);
            this.out = new PrintWriter(writer);
        }

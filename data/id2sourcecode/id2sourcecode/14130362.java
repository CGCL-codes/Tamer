        public ClassicReaderWriterFileDataSource(JUMPReader reader, JUMPWriter writer, String[] extensions) {
            super(new DelegatingCompressedFileHandler(reader, toEndings(extensions)), writer, extensions);
            this.extensions = extensions;
        }

        private WrappedFileInputStream(File file) throws FileNotFoundException {
            super(file);
            this.filesize = file.length();
            this.prevperc = -1;
            this.fc = getChannel();
        }

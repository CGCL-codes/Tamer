        public FileWriteListener(boolean useFileChannels) throws IOException {
            this.file = File.createTempFile("tmp", ".tmp", null);
            this.inputFile = new FileInputStream(file);
            this.fileChannel = inputFile.getChannel();
            this.useFileChannels = useFileChannels;
        }

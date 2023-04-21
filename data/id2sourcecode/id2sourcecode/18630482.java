    private void createChannel() throws IOException {
        if (channel_ == null) {
            channel_ = new FileInputStream(file_).getChannel();
        }
    }

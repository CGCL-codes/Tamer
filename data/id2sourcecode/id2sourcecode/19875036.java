    private void buildChannel() throws IOException {
        if (ch_ == null) {
            ch_ = new FileInputStream(f_).getChannel();
        }
    }

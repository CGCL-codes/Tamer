    @Override
    public void cleanup() {
        try {
            getChannel().close();
            _objInputStream.close();
            _byteInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

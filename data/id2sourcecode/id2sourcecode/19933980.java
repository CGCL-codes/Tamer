    public void flush() throws IOException {
        getTopStream().flush();
        theFile.getChannel().force(false);
    }

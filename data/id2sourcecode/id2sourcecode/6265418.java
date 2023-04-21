    public void endDocument() throws IOException {
        if (!documentStarted) {
            return;
        }
        fileStream.getChannel().force(true);
        outStream.close();
    }

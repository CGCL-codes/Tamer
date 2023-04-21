    public void selectChannel(String name) {
        for (Stream stream : streams) {
            for (SourceChannel sc : stream.getChannels()) {
                if (sc.getName().equals(name)) {
                    sc.apply(stream);
                    break;
                }
            }
        }
    }

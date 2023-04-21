    private void write(final Groups groups) throws ShowFileException {
        for (int i = 0; i < groups.size(); i++) {
            Group group = groups.get(i);
            write("GROUP");
            write(" \"" + group.getName() + "\" ");
            write(" " + group.size());
            Channel[] channels = group.getChannels();
            for (Channel channel : channels) {
                write(" " + (channel.getId() + 1));
            }
            writeln("");
        }
    }

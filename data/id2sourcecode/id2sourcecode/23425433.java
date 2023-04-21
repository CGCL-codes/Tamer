    private void write(final Dimmers dimmers) throws ShowFileException {
        for (int i = 0; i < dimmers.size(); i++) {
            Dimmer dimmer = dimmers.get(i);
            write("DIMMER");
            write(" " + (i + 1));
            writeQuoted(dimmer.getName());
            write(" " + (dimmer.getChannelId() + 1));
            writeln("");
        }
    }

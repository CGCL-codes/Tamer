    public void write(final Show show) throws ShowFileException {
        writeln("DIMMERS " + show.getNumberOfDimmers());
        writeln("CHANNELS " + show.getNumberOfChannels());
        writeln("SUBMASTERS " + show.getNumberOfSubmasters());
        write(show.getDimmers());
        write(show.getChannels());
        write(show.getSubmasters());
        write(show.getGroups());
        writeCues(show);
        write(show.getFrameProperties());
        try {
            file.close();
        } catch (IOException e) {
        }
    }

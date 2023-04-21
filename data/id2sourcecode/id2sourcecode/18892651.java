    public void exportToWriter(final Writer out, final ITVDataIterators data) throws IOException, Exception {
        writeHeader(out);
        Collection channels = data.getChannels();
        Iterator itCh = channels.iterator();
        while (itCh.hasNext()) {
            TVChannelsSet.Channel chinfo = (TVChannelsSet.Channel) itCh.next();
            TVChannel ch = data.getRealChannel(chinfo);
            writeChannelInfo(out, ch);
        }
        itCh = channels.iterator();
        while (itCh.hasNext()) {
            TVChannelsSet.Channel ch = (TVChannelsSet.Channel) itCh.next();
            Collection progs = data.getProgrammes(ch);
            final Iterator itP = progs.iterator();
            while (itP.hasNext()) {
                TVProgramme programme = (TVProgramme) itP.next();
                writeProgrammeInfo(out, programme);
            }
        }
        writeFooter(out);
    }

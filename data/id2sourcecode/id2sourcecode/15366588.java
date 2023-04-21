    private ModifiableTVData cloneTVData(final TVData in) {
        ModifiableTVData ret = new ModifiableTVData();
        Iterator itCh = in.getChannelsIterator();
        while (itCh.hasNext()) {
            TVChannel oldChannel = (TVChannel) itCh.next();
            TVChannel newChannel = new TVChannel(oldChannel.getID(), oldChannel.getDisplayName());
            Iterator itPr = oldChannel.getProgrammes().iterator();
            while (itPr.hasNext()) {
                TVProgramme oldProgramme = (TVProgramme) itPr.next();
                newChannel.put((TVProgramme) oldProgramme.clone());
            }
            ret.addChannel(newChannel);
        }
        return ret;
    }

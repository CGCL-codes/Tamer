    private List getChannels() {
        List channels = new ArrayList();
        Iterator it = TuxGuitar.instance().getSongManager().getChannels().iterator();
        while (it.hasNext()) {
            channels.add(cloneChannel((TGChannel) it.next()));
        }
        return channels;
    }

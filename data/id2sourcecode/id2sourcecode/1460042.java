    protected static void ParseChannels(Element root, Dialog dialog) {
        Element channels = root.getChild("channels");
        if (channels == null) return;
        Iterator i = channels.getChildren("channel").iterator();
        while (i.hasNext()) {
            DialogChannel dchannel = ParseChannel((Element) i.next());
            if (dchannel != null) dialog.getChannels().add(dchannel);
        }
    }

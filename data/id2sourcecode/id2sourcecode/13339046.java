    public Object clone() {
        Object cl = null;
        Channel chclone = null;
        try {
            cl = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        ((ChannelManager) cl).setChannels(new Vector());
        for (Iterator i = channels.iterator(); i.hasNext(); ) {
            Channel ch = (Channel) i.next();
            try {
                chclone = (Channel) ch.clone();
            } catch (CloneNotSupportedException e) {
            }
            ((ChannelManager) cl).getChannels().add(chclone);
        }
        return cl;
    }

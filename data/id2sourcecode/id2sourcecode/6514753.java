    @Override
    public String toString() {
        int i;
        StringBuffer rep = new StringBuffer("[ChannelDefinitionMapper nchannels= ").append(ncomps);
        for (i = 0; i < ncomps; ++i) {
            rep.append(eol).append("  component[").append(i).append("] mapped to channel[").append(csMap.getChannelDefinition(i)).append("]");
        }
        return rep.append("]").toString();
    }

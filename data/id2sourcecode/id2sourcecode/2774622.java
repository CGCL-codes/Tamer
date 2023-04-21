    public void connectSources() {
        for (int i = 0; i < srcConfigVec.size(); i++) {
            DswSource source = new DswSource(srcConfigVec.elementAt(i));
            sourceVec.addElement(source);
            boolean connected = source.connect();
            chnlIndexVec.addElement(source.getChannelIndicies());
            chnlNamesVec.addElement(source.getChannelNames());
        }
    }

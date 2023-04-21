    public void activateDaemons() {
        ChannelGuideSet theSet = GlobalModel.SINGLETON.getChannelGuideSet();
        theSet.getInformaBackEnd().activateMemoryChannels4CG(this);
    }

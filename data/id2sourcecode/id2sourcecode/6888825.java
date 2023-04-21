    public Old2NewDesign(MemberManager memberManager) {
        super();
        this.memberManager = memberManager;
        this.functionManager = memberManager.getFunctionManager();
        this.channelManager = memberManager.getChannelManager();
        this.studyManager = memberManager.getStudyManager();
    }

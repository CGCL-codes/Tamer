    @Override
    public void actionPerformed(MessageQueueEvent e) {
        if (e.getMsgtype() == MessageQueueEnum.CHANNEL_JOIN || e.getMsgtype() == MessageQueueEnum.CHANNEL_PART) {
            ChannelResponse cr = (ChannelResponse) e.getData();
            if (getChannel().equals(cr.getChannel())) {
                String action = "";
                if (e.getMsgtype() == MessageQueueEnum.CHANNEL_JOIN) action = "join"; else action = "part";
                getEmOutput().addText(ApplicationInfo.CLS_COLOR_ACTION, String.format(MessagesFormat.MSG_CHANNEL_ACTION, cr.getUser().getNick(), cr.getUser().getLogin(), cr.getUser().getHostmask(), action, cr.getChannel().getName()));
                updateUser(action, cr.getUser());
            }
        }
        if (e.getMsgtype() == MessageQueueEnum.MSG_CHANNEL_IN || e.getMsgtype() == MessageQueueEnum.MSG_CHANNEL_OUT) {
            ChannelMessageResponse mug = (ChannelMessageResponse) e.getData();
            if (getChannel().getName().toLowerCase().equals(mug.getChannel().getName().toLowerCase())) {
                getEmOutput().addText(ApplicationInfo.CLS_COLOR_USERMSG, String.format(MessagesFormat.MSG_USER, mug.getSender().getNick(), mug.getMessage()));
            }
        }
        if (e.getMsgtype() == MessageQueueEnum.CHANNEL_NOTICE) {
            GenericChannelResponse ccr = (GenericChannelResponse) e.getData();
            if (getChannel().equals(ccr.getChannel())) getEmOutput().addText(ApplicationInfo.CLS_COLOR_NOTICE, String.format(MessagesFormat.MSG_NOTICE, ccr.getUser().getNick(), ccr.getMessage()));
        }
        if (e.getMsgtype() == MessageQueueEnum.CHANNEL_TOPIC) {
            ChannelTopicResponse ctp = (ChannelTopicResponse) e.getData();
            if (getChannel().equals(ctp.getChannel())) {
                setTopic(ctp);
            }
        }
        if (e.getMsgtype() == MessageQueueEnum.IRC_MODE) {
            GenericChannelResponse gcr = (GenericChannelResponse) e.getData();
            if (gcr.getChannel() != null) {
                if (getChannel().equals(gcr.getChannel())) {
                    getEmOutput().addText(ApplicationInfo.CLS_COLOR_MODE, String.format(MessagesFormat.MSG_MODE, gcr.getUser(), gcr.getMessage()));
                }
            }
        }
        if (e.getMsgtype() == MessageQueueEnum.USER_QUIT) {
            GenericUserResponse gur = (GenericUserResponse) e.getData();
            if (gur.getSender().getChannels().contains(getChannel())) getEmOutput().addText(ApplicationInfo.CLS_COLOR_ACTION, String.format(MessagesFormat.MSG_CHANNEL_ACTION, gur.getSender().getNick(), gur.getSender().getLogin(), gur.getSender().getHostmask(), "quit", getChannel().getName()));
        }
        if (e.getMsgtype() == MessageQueueEnum.CHANNEL_USERLIST) {
            Channel ch = (Channel) e.getData();
            if (getChannel().equals(ch)) updateWho();
        }
        if (e.getMsgtype() == MessageQueueEnum.CHANNEL_OPERATION) {
            ChannelOperationResponse cor = (ChannelOperationResponse) e.getData();
            getEmOutput().addText(ApplicationInfo.CLS_COLOR_ACTION, String.format(MessagesFormat.MSG_CHANNEL_OPERATION, cor.getSender().getNick(), cor.getType().toString().toLowerCase(), cor.getTarget().getNick()));
            updateWho();
        }
        super.actionPerformed(e);
    }

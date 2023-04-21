    @Override
    public void actionPerformed(MessageQueueEvent e) {
        final MessageQueueEvent ev = e;
        if (e.getMsgtype() == MessageQueueEnum.GLOBAL || e.getMsgtype() == MessageQueueEnum.CONSOLE) {
            getEditor().addText(ApplicationInfo.CLS_COLOR_GLOBAL, String.format(MessagesFormat.MSG_GLOBAL, e.getData().toString()));
        }
        if (e.getMsgtype() == MessageQueueEnum.IRC_NOTICE) {
            GenericUserResponse gr = (GenericUserResponse) e.getData();
            getEditor().addText(ApplicationInfo.CLS_COLOR_NOTICE, String.format(MessagesFormat.MSG_NOTICE, gr.getSender().getNick(), gr.getText()));
        }
        if (e.getMsgtype() == MessageQueueEnum.IRC_MOTD) {
            getEditor().addText(ApplicationInfo.CLS_COLOR_GLOBAL, String.format(MessagesFormat.MSG_MOTD, e.getData().toString()));
        }
        if (e.getMsgtype() == MessageQueueEnum.IRC_MODE) {
            GenericChannelResponse gcr = (GenericChannelResponse) e.getData();
            getEditor().addText(ApplicationInfo.CLS_COLOR_MODE, String.format(MessagesFormat.MSG_MODE, gcr.getUser().getNick(), gcr.getMessage()));
        }
        if (e.getMsgtype() == MessageQueueEnum.IRC_USERMODE) {
            GenericTargetResponse gtr = (GenericTargetResponse) e.getData();
            getEditor().addText(ApplicationInfo.CLS_COLOR_MODE, String.format(MessagesFormat.MSG_USERMODE, gtr.getSender().getNick(), gtr.getTarget().getNick(), gtr.getMessage()));
        }
        if (e.getMsgtype() == MessageQueueEnum.IRC_CONNECTED) {
            getEditor().addText(ApplicationInfo.CLS_COLOR_GLOBAL, "Connected!");
            asyncExec(new Runnable() {

                @Override
                public void run() {
                    tltmDisconnect.setEnabled(true);
                }
            });
        }
        if (e.getMsgtype() == MessageQueueEnum.IRC_DISCONNECTED) {
            getEditor().addText(ApplicationInfo.CLS_COLOR_GLOBAL, "Disconnected!");
            asyncExec(new Runnable() {

                @Override
                public void run() {
                    tltmDisconnect.setEnabled(false);
                }
            });
        }
        if (e.getMsgtype() == MessageQueueEnum.IRC_CONNECTING) {
            getEditor().addText(ApplicationInfo.CLS_COLOR_GLOBAL, "Connecting to %s:%s", ((ConnectionSettings) e.getData()).getServer(), ((ConnectionSettings) e.getData()).getPort());
        }
        if (e.getMsgtype() == MessageQueueEnum.IRC_ERROR_CHANNEL) {
            ChannelErrorResponse cer = (ChannelErrorResponse) e.getData();
            getEditor().addText(ApplicationInfo.CLS_COLOR_ERROR, String.format(MessagesFormat.MSG_CHANNEL_ERROR, cer.getChannel().getName(), cer.getMessage()));
        }
        if (e.getMsgtype() == MessageQueueEnum.DCC_FILE_INCOMING || e.getMsgtype() == MessageQueueEnum.DCC_FILE_OUTCOMING) {
            asyncExec(new Runnable() {

                @Override
                public void run() {
                    if (!TabManager.checkTabExists(ApplicationInfo.TAB_DOWNLOADS)) {
                        TabManager.addTab(ApplicationInfo.TAB_DOWNLOADS, "/com/google/code/cubeirc/resources/img_send.png", true, DownloadsForm.class.getName(), new Object[] { TabManager.getTabfolder().getParent(), SWT.NORMAL, ApplicationInfo.TAB_DOWNLOADS }, new Class[] { Composite.class, int.class, String.class });
                        MessageQueue.addQueue(ev.getMsgtype(), ev.getData());
                    }
                }
            });
        }
        super.actionPerformed(e);
    }

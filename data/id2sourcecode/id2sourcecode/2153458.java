            @Override
            public void run() {
                if (data.getUser().equals(Connection.getUserInfo())) {
                    if (!TabManager.checkTabExists(data.getChannel().getName().toLowerCase())) {
                        String channel = data.getChannel().getName().toLowerCase();
                        TabManager.addTab(channel, "/com/google/code/cubeirc/resources/img_channel.png", true, ChannelForm.class.getName(), new Object[] { TabManager.getTabfolder().getParent(), SWT.NORMAL, channel, data.getChannel() }, new Class[] { Composite.class, int.class, String.class, Channel.class });
                        MessageQueue.addQueue(type, data);
                    }
                }
            }

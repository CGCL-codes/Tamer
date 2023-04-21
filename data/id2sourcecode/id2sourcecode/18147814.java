    public void storeTabStates() {
        if (!Raptor.getInstance().getWindow().getShell().isDisposed()) {
            String preference = "";
            RaptorConnectorWindowItem[] items = Raptor.getInstance().getWindow().getWindowItems(this);
            Arrays.sort(items, new Comparator<RaptorConnectorWindowItem>() {

                public int compare(RaptorConnectorWindowItem arg0, RaptorConnectorWindowItem arg1) {
                    if (arg0 instanceof ChatConsoleWindowItem && arg1 instanceof ChatConsoleWindowItem) {
                        ChatConsoleWindowItem chatConsole1 = (ChatConsoleWindowItem) arg0;
                        ChatConsoleWindowItem chatConsole2 = (ChatConsoleWindowItem) arg1;
                        if (chatConsole1.getController() instanceof ChannelController && chatConsole2.getController() instanceof ChannelController) {
                            Integer integer1 = new Integer(((ChannelController) chatConsole1.getController()).getChannel());
                            Integer integer2 = new Integer(((ChannelController) chatConsole2.getController()).getChannel());
                            return integer1.compareTo(integer2);
                        } else if (!(chatConsole1.getController() instanceof ChannelController) && chatConsole2.getController() instanceof ChannelController) {
                            return 1;
                        } else if (chatConsole1.getController() instanceof ChannelController && !(chatConsole2.getController() instanceof ChannelController)) {
                            return -1;
                        } else {
                            return 0;
                        }
                    } else if (arg0 instanceof ChatConsoleWindowItem && !(arg1 instanceof ChatConsoleWindowItem)) {
                        return -1;
                    } else if (arg1 instanceof ChatConsoleWindowItem && !(arg0 instanceof ChatConsoleWindowItem)) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });
            for (RaptorConnectorWindowItem item : items) {
                if (item instanceof ChatConsoleWindowItem) {
                    ChatConsoleWindowItem chatConsoleItem = (ChatConsoleWindowItem) item;
                    if (chatConsoleItem.getController() instanceof ChannelController) {
                        ChannelController controller = (ChannelController) chatConsoleItem.getController();
                        preference += (preference.isEmpty() ? "" : "`") + "Channel`" + controller.getChannel() + "`" + Raptor.getInstance().getWindow().getQuadrant(item).toString();
                    } else if (chatConsoleItem.getController() instanceof RegExController) {
                        RegExController controller = (RegExController) chatConsoleItem.getController();
                        preference += (preference.isEmpty() ? "" : "`") + "RegEx`" + controller.getPattern() + "`" + Raptor.getInstance().getWindow().getQuadrant(item).toString();
                    }
                } else if (item instanceof SeekTableWindowItem) {
                    preference += (preference.isEmpty() ? "" : "`") + "SeekTableWindowItem` " + "` ";
                } else if (item instanceof BugWhoWindowItem) {
                    preference += (preference.isEmpty() ? "" : "`") + "BugWhoWindowItem` " + "` ";
                } else if (item instanceof BugButtonsWindowItem) {
                    preference += (preference.isEmpty() ? "" : "`") + "BugButtonsWindowItem` " + "` ";
                } else if (item instanceof GamesWindowItem) {
                    preference += (preference.isEmpty() ? "" : "`") + "GamesWindowItem` ` ";
                }
            }
            Raptor.getInstance().getPreferences().setValue(context.getShortName() + "-" + currentProfileName + "-" + PreferenceKeys.CHANNEL_REGEX_TAB_INFO, preference);
            Raptor.getInstance().getPreferences().save();
        }
    }

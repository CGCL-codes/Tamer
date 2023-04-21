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

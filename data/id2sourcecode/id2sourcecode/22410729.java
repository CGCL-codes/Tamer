            @Override
            public void run() {
                try {
                    String ip = null;
                    if (cb_useHamachi.isSelected()) {
                        ip = roomData.getHamachiIP();
                    } else {
                        ip = roomData.getIP();
                    }
                    LaunchMethods method = GameDatabase.getLaunchMethod(roomData.getChannel(), roomData.getModName());
                    if (method == LaunchMethods.PARAMETER) {
                        launchInfo = new ParameterLaunchInfo(roomData);
                    } else {
                        launchInfo = new DirectPlayLaunchInfo(roomData);
                    }
                    Launcher.initialize(launchInfo);
                } catch (Exception e) {
                    ErrorHandler.handleException(e);
                }
            }

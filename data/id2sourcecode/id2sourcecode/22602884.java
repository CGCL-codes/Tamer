            @Override
            public void run() {
                try {
                    LaunchMethods method = GameDatabase.getLaunchMethod(roomData.getChannel(), roomData.getModName());
                    if (method == LaunchMethods.PARAMETER) {
                        launchInfo = new ParameterLaunchInfo(roomData);
                    } else if (method == LaunchMethods.DOS) {
                        launchInfo = new DosboxLaunchInfo(roomData);
                    } else {
                        launchInfo = new DirectPlayLaunchInfo(roomData);
                    }
                    Launcher.initialize(launchInfo);
                } catch (Exception e) {
                    ErrorHandler.handle(e);
                }
            }

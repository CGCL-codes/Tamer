    private UILabModule getModuleByIDAndClassnameAndAddressAndChannel(String identifier, String classname, int address, String channel) {
        UILabModule foundModule = null;
        for (UILabModule currModule : this.uiModules) {
            if (currModule.getBoard() == null) {
                if (currModule.getClass().getName().equals(classname) && address == -1 && currModule.getId().equals(identifier)) {
                    foundModule = currModule;
                    break;
                }
            } else {
                if (currModule.getClass().getName().equals(classname) && currModule.getBoard().getAddress() == address && currModule.getBoard().getCommChannel().getChannelName().equals(channel) && currModule.getId().equals(identifier)) {
                    foundModule = currModule;
                    break;
                }
            }
        }
        return foundModule;
    }

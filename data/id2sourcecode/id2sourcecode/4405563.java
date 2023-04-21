    public CTModuleParameterConfig getCTModuleConfigByTypeAndBoard(String type, Board board) {
        CTModuleParameterConfig foundParameters = null;
        if (ctModuleParams != null) {
            for (CTModuleParameterConfig currParameters : ctModuleParams) {
                if (currParameters.getModuleType().equals(type) && currParameters.getChannelName().equals(board.getCommChannel().getChannelName()) && currParameters.getAddress() == board.getAddress()) {
                    foundParameters = currParameters;
                    break;
                }
            }
        }
        return foundParameters;
    }

    private String System() {
        getParams();
        FtpConstraintLimitHandler handler = FileBasedConfiguration.fileBasedConfiguration.constraintLimitHandler;
        if (params == null) {
            String system = REQUEST.System.readFileUnique();
            StringBuilder builder = new StringBuilder(system);
            GgStringUtils.replace(builder, "XXXXCHANNELLIMITRXXX", Long.toString(FileBasedConfiguration.fileBasedConfiguration.getServerGlobalReadLimit()));
            GgStringUtils.replace(builder, "XXXXCPULXXX", Double.toString(handler.getCpuLimit()));
            GgStringUtils.replace(builder, "XXXXCONLXXX", Integer.toString(handler.getChannelLimit()));
            GgStringUtils.replace(builder, "XXXRESULTXXX", "");
            return builder.toString();
        }
        String extraInformation = null;
        if (params.containsKey("ACTION")) {
            List<String> action = params.get("ACTION");
            for (String act : action) {
                if (act.equalsIgnoreCase("Disconnect")) {
                    String logon = Logon();
                    newSession = true;
                    clearSession();
                    forceClose = true;
                    return logon;
                } else if (act.equalsIgnoreCase("Shutdown")) {
                    String error = error("Shutdown in progress");
                    newSession = true;
                    clearSession();
                    forceClose = true;
                    shutdown = true;
                    return error;
                } else if (act.equalsIgnoreCase("Validate")) {
                    String bglobalr = getTrimValue("BGLOBR");
                    long lglobal = FileBasedConfiguration.fileBasedConfiguration.getServerGlobalReadLimit();
                    if (bglobalr != null) {
                        lglobal = Long.parseLong(bglobalr);
                    }
                    FileBasedConfiguration.fileBasedConfiguration.changeNetworkLimit(lglobal, lglobal);
                    bglobalr = getTrimValue("CPUL");
                    double dcpu = handler.getCpuLimit();
                    if (bglobalr != null) {
                        dcpu = Double.parseDouble(bglobalr);
                    }
                    handler.setCpuLimit(dcpu);
                    bglobalr = getTrimValue("CONL");
                    int iconn = handler.getChannelLimit();
                    if (bglobalr != null) {
                        iconn = Integer.parseInt(bglobalr);
                    }
                    handler.setChannelLimit(iconn);
                    extraInformation = "Configuration Saved";
                }
            }
        }
        String system = REQUEST.System.readFileUnique();
        StringBuilder builder = new StringBuilder(system);
        GgStringUtils.replace(builder, "XXXXCHANNELLIMITRXXX", Long.toString(FileBasedConfiguration.fileBasedConfiguration.getServerGlobalReadLimit()));
        GgStringUtils.replace(builder, "XXXXCPULXXX", Double.toString(handler.getCpuLimit()));
        GgStringUtils.replace(builder, "XXXXCONLXXX", Integer.toString(handler.getChannelLimit()));
        if (extraInformation != null) {
            GgStringUtils.replace(builder, "XXXRESULTXXX", extraInformation);
        } else {
            GgStringUtils.replace(builder, "XXXRESULTXXX", "");
        }
        return builder.toString();
    }

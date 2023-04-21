    public CommandResponse doSITE_BLOWFISH(CommandRequest request) {
        Session session = request.getSession();
        if (!session.isSecure()) {
            return new CommandResponse(530, session.jprintf(_bundle, _keyPrefix + "blowfish.reject", request.getUser()));
        }
        SiteBotWrapper wrapper = null;
        for (PluginInterface plugin : GlobalContext.getGlobalContext().getPlugins()) {
            if (plugin instanceof SiteBotWrapper) {
                wrapper = (SiteBotWrapper) plugin;
                break;
            }
        }
        if (wrapper == null) {
            return new CommandResponse(500, "No SiteBots loaded");
        }
        String botName = null;
        if (request.hasArgument()) {
            StringTokenizer st = new StringTokenizer(request.getArgument());
            botName = st.nextToken();
        } else {
            botName = wrapper.getBots().get(0).getBotName();
        }
        User user = session.getUserNull(request.getUser());
        SiteBot bot = null;
        for (SiteBot currBot : wrapper.getBots()) {
            if (currBot.getBotName().equalsIgnoreCase(botName)) {
                bot = currBot;
                break;
            }
        }
        if (bot == null) {
            return new CommandResponse(500, "No Blowfish keys found");
        }
        ArrayList<String> outputKeys = new ArrayList<String>();
        for (ChannelConfig chan : bot.getConfig().getChannels()) {
            if (chan.getBlowKey() != null) {
                if (chan.isPermitted(user) && !chan.getBlowKey().equals("")) {
                    outputKeys.add(chan.getName() + " - " + chan.getBlowKey());
                }
            }
        }
        if (outputKeys.size() == 0) {
            return new CommandResponse(500, "No Blowfish keys found");
        }
        CommandResponse response = StandardCommandManager.genericResponse("RESPONSE_200_COMMAND_OK");
        response.addComment(bot.getBotName() + ":");
        Collections.sort(outputKeys);
        for (String chanKey : outputKeys) {
            response.addComment(chanKey);
        }
        return response;
    }

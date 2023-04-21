    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Collection errors = new ArrayList();
        String channelName = request.getParameter("channel");
        Settings settings = null;
        if (errors.isEmpty()) {
            if (channelName != null) {
                Channel channel = ChannelManager.getInstance().getChannel(channelName);
                settings = channel.getConfig().getSettings();
            } else {
                settings = Settings.getDefaultSettings();
            }
        }
        boolean resetSpecials = true;
        Occurancy<Special> specialOccurancy = settings.getSpecialOccurancy().clone();
        for (Special special : Special.values()) {
            String value = request.getParameter(special.getCode());
            resetSpecials = resetSpecials && StringUtils.isBlank(value);
            if (StringUtils.isNotBlank(value)) {
                specialOccurancy.setOccurancy(special, Integer.parseInt(value));
            }
        }
        if (!specialOccurancy.equals(settings.getSpecialOccurancy())) {
            specialOccurancy.normalize();
            settings.setSpecialOccurancy(specialOccurancy);
        } else if (resetSpecials) {
            settings.setDefaultSpecialOccurancy(true);
        }
        boolean resetBlocks = true;
        Occurancy<Block> blockOccurancy = settings.getBlockOccurancy().clone();
        for (Block block : Block.values()) {
            String value = request.getParameter(block.getCode());
            resetBlocks = resetBlocks && StringUtils.isBlank(value);
            if (StringUtils.isNotBlank(value)) {
                blockOccurancy.setOccurancy(block, Integer.parseInt(value));
            }
        }
        if (!blockOccurancy.equals(settings.getBlockOccurancy())) {
            blockOccurancy.normalize();
            settings.setBlockOccurancy(blockOccurancy);
        } else if (resetBlocks) {
            settings.setDefaultBlockOccurancy(true);
        }
        updateSettingsField(settings, "startingLevel", request);
        updateSettingsField(settings, "stackHeight", request);
        updateSettingsField(settings, "linesPerLevel", request);
        updateSettingsField(settings, "linesPerSpecial", request);
        updateSettingsField(settings, "levelIncrease", request);
        updateSettingsField(settings, "specialAdded", request);
        updateSettingsField(settings, "specialCapacity", request);
        updateSettingsField(settings, "averageLevels", request);
        updateSettingsField(settings, "classicRules", request);
        updateSettingsField(settings, "sameBlocks", request);
        updateSettingsField(settings, "suddenDeathTime", request);
        updateSettingsField(settings, "suddenDeathMessage", request);
        updateSettingsField(settings, "suddenDeathDelay", request);
        updateSettingsField(settings, "suddenDeathLinesAdded", request);
        if (settings != Settings.getDefaultSettings()) {
            response.sendRedirect("/channel.jsp?name=" + channelName);
        } else {
            response.sendRedirect("/server.jsp");
        }
        Server.getInstance().getConfig().save();
    }

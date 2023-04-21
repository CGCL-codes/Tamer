    public void execute(CommandMessage m) {
        Client client = (Client) m.getSource();
        Locale locale = client.getUser().getLocale();
        Settings s = client.getChannel().getConfig().getSettings();
        String configLines[] = new String[19];
        configLines[0] = "<darkBlue>" + "<b>" + Language.getText("command.config.blocks", locale) + "			" + Language.getText("command.config.specials", locale);
        configLines[1] = "<darkBlue>" + LEFTL.getName(locale) + "	: <purple>" + s.getOccurancy(LEFTL) + "%</purple>		(<purple>A</purple>) " + ADDLINE.getName(locale) + "	: " + "<purple>" + s.getOccurancy(ADDLINE) + "%";
        configLines[2] = "<darkBlue>" + RIGHTL.getName(locale) + "	: <purple>" + s.getOccurancy(RIGHTL) + "%</purple>		(<purple>C</purple>) " + CLEARLINE.getName(locale) + "	: " + "<purple>" + s.getOccurancy(CLEARLINE) + "%";
        configLines[3] = "<darkBlue>" + SQUARE.getName(locale) + "	: <purple>" + s.getOccurancy(SQUARE) + "%</purple>		(<purple>N</purple>) " + NUKEFIELD.getName(locale) + "	: " + "<purple>" + s.getOccurancy(NUKEFIELD) + "%";
        configLines[4] = "<darkBlue>" + LEFTZ.getName(locale) + "	: <purple>" + s.getOccurancy(LEFTZ) + "%</purple>		(<purple>R</purple>) " + RANDOMCLEAR.getName(locale) + "	: " + "<purple>" + s.getOccurancy(RANDOMCLEAR) + "%";
        configLines[5] = "<darkBlue>" + RIGHTZ.getName(locale) + "	: <purple>" + s.getOccurancy(RIGHTZ) + "%</purple>		(<purple>S</purple>) " + SWITCHFIELD.getName(locale) + "	: " + "<purple>" + s.getOccurancy(SWITCHFIELD) + "%";
        configLines[6] = "<darkBlue>" + HALFCROSS.getName(locale) + "	: <purple>" + s.getOccurancy(HALFCROSS) + "%</purple>		(<purple>B</purple>) " + CLEARSPECIAL.getName(locale) + "	: " + "<purple>" + s.getOccurancy(CLEARSPECIAL) + "%";
        configLines[7] = "<darkBlue>" + LINE.getName(locale) + "	: <purple>" + s.getOccurancy(LINE) + "%</purple>		(<purple>G</purple>) " + GRAVITY.getName(locale) + "	: " + "<purple>" + s.getOccurancy(GRAVITY) + "%";
        configLines[8] = "<darkBlue>" + "			(<purple>Q</purple>) " + QUAKEFIELD.getName(locale) + "	: <purple>" + s.getOccurancy(QUAKEFIELD) + "%";
        configLines[9] = "<darkBlue>" + "<b>" + Language.getText("command.config.rules", locale) + "<b>" + "			(<purple>O<purple>) " + BLOCKBOMB.getName(locale) + "	: <purple>" + s.getOccurancy(BLOCKBOMB) + "%";
        configLines[10] = "<darkBlue>" + Language.getText("command.config.rules.starting_level", locale) + "	: " + "<blue>" + s.getStartingLevel();
        configLines[11] = "<darkBlue>" + Language.getText("command.config.rules.lines_per_level", locale) + "	: " + "<blue>" + s.getLinesPerLevel();
        configLines[12] = "<darkBlue>" + Language.getText("command.config.rules.level_increase", locale) + "	: " + "<blue>" + s.getLevelIncrease();
        configLines[13] = "<darkBlue>" + Language.getText("command.config.rules.lines_per_special", locale) + "	: " + "<blue>" + s.getLinesPerSpecial();
        configLines[14] = "<darkBlue>" + Language.getText("command.config.rules.special_added", locale) + "	: " + "<blue>" + s.getSpecialAdded();
        configLines[15] = "<darkBlue>" + Language.getText("command.config.rules.special_capacity", locale) + "	: " + "<blue>" + s.getSpecialCapacity();
        configLines[16] = "<darkBlue>" + Language.getText("command.config.rules.classic_rules", locale) + "	: " + "<blue>" + (s.getClassicRules() ? Language.getText("common.yes", locale) : Language.getText("common.no", locale));
        configLines[17] = "<darkBlue>" + Language.getText("command.config.rules.average_levels", locale) + "	: " + "<blue>" + (s.getAverageLevels() ? Language.getText("common.yes", locale) : Language.getText("common.no", locale));
        configLines[18] = "<darkBlue>" + Language.getText("command.config.rules.same_blocks", locale) + "	: " + "<blue>" + (s.getSameBlocks() ? Language.getText("common.yes", locale) : Language.getText("common.no", locale));
        for (int i = 0; i < configLines.length; i++) {
            Message configMessage = new PlineMessage(configLines[i]);
            client.send(configMessage);
        }
    }

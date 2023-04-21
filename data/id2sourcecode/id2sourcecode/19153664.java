    public void initConfig() {
        dtm.setRowCount(0);
        RunConfiguration runConf = theLab.getConfig().getRunConfiguration();
        if (runConf != null) {
            for (Run currRun : runConf.getRuns()) {
                Board board = theLab.getBoardForCommChannelNameAndAddress(currRun.getChannel(), currRun.getAddress());
                String identifier = "???";
                if (board != null) {
                    identifier = board.getBoardIdentifier();
                }
                RunDefinitionAnalyzed defAnalyzed = theLab.getRunSetsPerRunDefinition().get(currRun.getName());
                dtm.addRow(new Object[] { currRun.getName(), identifier + " - " + currRun.getChannel() + "(" + currRun.getAddress() + ")", defAnalyzed.getParameter() });
            }
        }
        jComboBoxRun.removeAllItems();
        if (theLab.getConfig().getRunDefinitions() != null) {
            for (RunDefinition runDef : theLab.getConfig().getRunDefinitions()) {
                jComboBoxRun.addItem(runDef);
            }
        }
        jComboBoxBoard.removeAllItems();
        for (Board currBoard : theLab.getAllBoardsFound()) {
            jComboBoxBoard.addItem(currBoard);
        }
    }

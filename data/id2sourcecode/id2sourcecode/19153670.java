    private void addRun() {
        Run newRun = new Run();
        RunDefinition selectedRunDef = (RunDefinition) jComboBoxRun.getSelectedItem();
        Board selectedBoard = (Board) jComboBoxBoard.getSelectedItem();
        newRun.setName(selectedRunDef.getName());
        newRun.setChannel(selectedBoard.getCommChannel().getChannelName());
        newRun.setAddress(selectedBoard.getAddress());
        dtm.addRow(new Object[] { selectedRunDef.getName(), selectedBoard.getBoardIdentifier() + " - " + newRun.getChannel() + "(" + newRun.getAddress() + ")", selectedRunDef.getParameter() });
        theLab.getConfig().addRunConfigurationRun(newRun);
    }

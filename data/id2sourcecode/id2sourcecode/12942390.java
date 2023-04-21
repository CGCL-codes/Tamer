    private void addNewBoard(String bname, String bpubkey, String bprivkey, String description) {
        if (getBoardByName(bname) != null) {
            int answer = JOptionPane.showConfirmDialog(getTopLevelAncestor(), languageResource.getString("You already have a board with name") + " '" + bname + "'!\n" + languageResource.getString("Do you really want to overwrite it?") + "" + "\n(" + languageResource.getString("This will not delete messages") + ")", languageResource.getString("Warning"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (answer == JOptionPane.NO_OPTION) {
                return;
            }
        }
        FrostBoardObject newBoard = new FrostBoardObject(bname, bpubkey, bprivkey, description);
        addNodeToTree(newBoard);
        TOF.initialSearchNewMessages(newBoard);
    }

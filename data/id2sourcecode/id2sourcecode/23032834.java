    public void computeResult(JPAExecutorResult result) {
        this.result = result;
        this.progressCircle.setVisible(false);
        if (result.hasErrors()) {
            writeErrorMessage("Error executing Query: " + result.getErrorMessage());
        } else {
            JPAHistoryListManager.addStatementToHistory(lastCommand);
            jList1.setListData(JPAHistoryListManager.getHistory());
            writeSuccessMessage(String.format("Result %s - %s | %s", actualStartPosition, result.getResultList().size() > ResetViewThread.totalAmountOfItemsToDisplay ? actualStartPosition + ResetViewThread.totalAmountOfItemsToDisplay : result.getResultList().size(), result.getResultList().size()));
            actualResultSize = result.getResultList().size();
            JPAResultTreeNode root = new JPAResultTreeNode();
            root.setName("Result");
            root.setValue("Rückgabewerte des ausgefürten Query");
            root.setClass(JPAResultTreeNode.class);
            SwingUtilities.invokeLater(new ResetViewThread(root, this.result, treeMdl, mdl, outline, actualStartPosition));
            jTabbedPane1.setSelectedIndex(0);
        }
    }

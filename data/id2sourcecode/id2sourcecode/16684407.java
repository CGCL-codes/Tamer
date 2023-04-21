    private void completeConfigureItViaEditMenu() {
        Utils.logToConsole("Completing ENABLEAPI configuration");
        JDialog configDialog = TwsListener.getConfigDialog();
        if (configDialog == null) {
            System.err.println("IBControllerServer: could not find the Global Configuration dialog");
            mChannel.writeNack("Global Configuration dialog not found");
            return;
        }
        JTree configTree = Utils.findTree(configDialog);
        if (configTree == null) {
            System.err.println("IBControllerServer: could not find the config tree in the Global Configuration dialog");
            mChannel.writeNack("config tree not found");
            return;
        }
        TreePath tp = new TreePath(configTree.getModel().getRoot());
        Object node = Utils.findChildNode(configTree.getModel(), configTree.getModel().getRoot(), "API");
        tp = tp.pathByAddingChild(node);
        node = Utils.findChildNode(configTree.getModel(), node, "Settings");
        if (!(node == null)) tp = tp.pathByAddingChild(node);
        Utils.logToConsole("getExpandsSelectedPaths = " + configTree.getExpandsSelectedPaths());
        Utils.logToConsole("Selection path = " + tp.toString());
        configTree.setSelectionPath(tp);
        JCheckBox cb = Utils.findCheckBox(configDialog, "Enable ActiveX and Socket Clients");
        if (cb == null) {
            System.err.println("IBControllerServer: Could not find Enable ActiveX checkbox inside API menu.");
            mChannel.writeNack("Enable ActiveX checkbox not found");
            return;
        }
        if (!cb.isSelected()) {
            cb.doClick();
            Utils.clickButton(configDialog, "OK");
            Utils.logToConsole("TWS has been configured to accept API connections.");
            mChannel.writeAck("configured");
        } else {
            Utils.logToConsole("TWS is already configured to accept API connections.");
            mChannel.writeAck("already configured");
        }
        configDialog.setVisible(false);
    }

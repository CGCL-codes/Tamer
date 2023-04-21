    private JSplitPane getJSplitPane1() {
        if (jSplitPane1 == null) {
            jSplitPane1 = new JSplitPane();
            jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
            jSplitPane1.setDividerLocation(230);
            jSplitPane1.setDividerSize(2);
            jSplitPane1.setTopComponent(getChannelPropertyPanel());
            jSplitPane1.setBottomComponent(getJTabbedPane());
        }
        return jSplitPane1;
    }

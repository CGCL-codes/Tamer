    private JSplitPane getJSplitPane() {
        if (jSplitPane == null) {
            jSplitPane = new JSplitPane();
            jSplitPane.setLeftComponent(getChannelListingPanel());
            jSplitPane.setDividerSize(2);
            jSplitPane.setDividerLocation(300);
            jSplitPane.setEnabled(true);
            jSplitPane.setRightComponent(getJSplitPane1());
        }
        return jSplitPane;
    }

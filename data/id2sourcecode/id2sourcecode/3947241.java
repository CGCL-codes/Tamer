    private Widget getDetailPanel() {
        channelTree = new AccessChannelTreeWidget();
        functionTree = new AccessFunctionTreeWidget();
        form = new FormContainer(2);
        form.addElement(RolePage.getFunctionAccess(functionTree));
        form.addElement(RolePage.getChannelAccess(channelTree));
        form.getContainer().getCellFormatter().setVerticalAlignment(0, 0, VerticalPanel.ALIGN_TOP);
        form.getContainer().getCellFormatter().setVerticalAlignment(0, 1, VerticalPanel.ALIGN_TOP);
        return form;
    }

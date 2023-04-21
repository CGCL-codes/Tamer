    protected void createDiagramAction(GraphicalViewer viewer) {
        addAttributeAction = new AddAttributeAction(viewer.getEditDomain().getCommandStack(), viewer);
        addOperationAction = new AddOperationAction(viewer.getEditDomain().getCommandStack(), viewer);
        upAction = new UpAction(viewer.getEditDomain().getCommandStack(), viewer);
        downAction = new DownAction(viewer.getEditDomain().getCommandStack(), viewer);
        autoLayoutAction = new AutoLayoutAction(viewer);
        togglePublicAttr = new ToggleAction(UMLPlugin.getDefault().getResourceString("filter.attr.public"), viewer, ToggleAction.ATTRIBUTE, Visibility.PUBLIC);
        toggleProtectedAttr = new ToggleAction(UMLPlugin.getDefault().getResourceString("filter.attr.protected"), viewer, ToggleAction.ATTRIBUTE, Visibility.PROTECTED);
        togglePackageAttr = new ToggleAction(UMLPlugin.getDefault().getResourceString("filter.attr.package"), viewer, ToggleAction.ATTRIBUTE, Visibility.PACKAGE);
        togglePrivateAttr = new ToggleAction(UMLPlugin.getDefault().getResourceString("filter.attr.private"), viewer, ToggleAction.ATTRIBUTE, Visibility.PRIVATE);
        togglePublicOpe = new ToggleAction(UMLPlugin.getDefault().getResourceString("filter.ope.public"), viewer, ToggleAction.OPERATION, Visibility.PUBLIC);
        toggleProtectedOpe = new ToggleAction(UMLPlugin.getDefault().getResourceString("filter.ope.protected"), viewer, ToggleAction.OPERATION, Visibility.PROTECTED);
        togglePackageOpe = new ToggleAction(UMLPlugin.getDefault().getResourceString("filter.ope.package"), viewer, ToggleAction.OPERATION, Visibility.PACKAGE);
        togglePrivateOpe = new ToggleAction(UMLPlugin.getDefault().getResourceString("filter.ope.private"), viewer, ToggleAction.OPERATION, Visibility.PRIVATE);
    }

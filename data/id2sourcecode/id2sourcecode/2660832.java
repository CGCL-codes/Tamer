    private void doCreateMenuAction(IMenuManager manager, final GraphicalViewer viewer, final ArrayList<UiElementNode> selected) {
        if (selected != null) {
            boolean hasXml = false;
            for (UiElementNode uiNode : selected) {
                if (uiNode.getXmlNode() != null) {
                    hasXml = true;
                    break;
                }
            }
            if (hasXml) {
                manager.add(new CopyCutAction(mLayoutEditor, getClipboard(), null, selected, true));
                manager.add(new CopyCutAction(mLayoutEditor, getClipboard(), null, selected, false));
                if (selected.size() <= 1) {
                    UiElementNode ui_root = selected.get(0).getUiRoot();
                    if (ui_root.getDescriptor().hasChildren() || !(ui_root.getUiParent() instanceof UiDocumentNode)) {
                        manager.add(new PasteAction(mLayoutEditor, getClipboard(), selected.get(0)));
                    }
                }
                manager.add(new Separator());
            }
        }
        IconFactory factory = IconFactory.getInstance();
        final UiEditorActions uiActions = mLayoutEditor.getUiEditorActions();
        if (selected == null || selected.size() <= 1) {
            manager.add(new Action("Add...", factory.getImageDescriptor("add")) {

                @Override
                public void run() {
                    UiElementNode node = selected != null && selected.size() > 0 ? selected.get(0) : null;
                    uiActions.doAdd(node, viewer.getControl().getShell());
                }
            });
        }
        if (selected != null) {
            manager.add(new Action("Remove", factory.getImageDescriptor("delete")) {

                @Override
                public void run() {
                    uiActions.doRemove(selected, viewer.getControl().getShell());
                }
            });
            manager.add(new Separator());
            manager.add(new Action("Up", factory.getImageDescriptor("up")) {

                @Override
                public void run() {
                    uiActions.doUp(selected);
                }
            });
            manager.add(new Action("Down", factory.getImageDescriptor("down")) {

                @Override
                public void run() {
                    uiActions.doDown(selected);
                }
            });
        }
    }

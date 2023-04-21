    protected void changeName() {
        IStructuredSelection selection = (IStructuredSelection) getGraphicalViewer().getSelection();
        Object objSel = selection.getFirstElement();
        TreeNode node;
        boolean editable = true;
        if (!(objSel instanceof NodeEditPart)) return;
        Node csdeNode = ((NodeEditPart) objSel).getNode();
        node = csdeNode.getTemporaryTreeNode();
        if (node instanceof CUnitItem) return;
        if (node instanceof CPage) editable = csdeNode.getParent().getParent().isLocked(); else if (!csdeNode.isLocked()) editable = false;
        if (node instanceof CUnitItem) editable = false;
        String nodeName = VidUtil.getNodeName(node);
        String prefix = null;
        if (node instanceof CCourse) {
            prefix = nodeName.substring(nodeName.lastIndexOf("."));
            nodeName = nodeName.substring(0, nodeName.lastIndexOf("."));
        }
        SingleLineInputDialog dialog = new SingleLineInputDialog(getGraphicalControl().getShell(), I18N.translate("csdeEditor.nameChange.title"), I18N.translate("csdeEditor.nameChange.msg"), nodeName, null, editable);
        if (dialog.open() != Dialog.OK) return;
        VidUtil.setNodeName(node, dialog.getValue());
        ((NodeEditPart) objSel).getNode().setProperty(Node.TITLE_PROP, dialog.getValue());
        ((NodeEditPart) objSel).getNode().setProperty(Node.LABEL_PROP, dialog.getValue());
        if (prefix != null) ((NodeEditPart) objSel).getNode().setProperty(Node.EDITOR_TITLE, dialog.getValue() + prefix);
        IEditorPart editor = getSite().getPage().getActiveEditor();
        IViewPart view = getSite().getPage().findView(IPageLayout.ID_PROP_SHEET);
        if (view != null) {
            PropertySheet propertyView = (PropertySheet) view;
            propertyView.selectionChanged(editor, editor.getSite().getSelectionProvider().getSelection());
        }
    }

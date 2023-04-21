    private void updatePopupMenu(TreePath[] paths) {
        if (paths != null) {
            if (paths.length <= 1) {
                if (((MutableTreeNode) paths[0].getLastPathComponent()).isLeaf()) popupMenuExecute.setText("Open"); else {
                    if (tree.isExpanded(paths[0])) popupMenuExecute.setText("Collapse"); else popupMenuExecute.setText("Expand");
                }
                popupMenu.removeAll();
                if ((!((MutableTreeNode) paths[0].getLastPathComponent()).isLeaf()) | (getSelectedFile() != null && getSelectedFile().exists() && !getSelectedFile().isDirectory())) popupMenu.add(popupMenuExecute);
                if ((getCurrentFile() != null && !getCurrentFile().isDirectory()) && (getInfo().getProjectManager().getFileAssociationsManager().getAssociatedEditors(FileUtils.getExtension(getCurrentFile())) != null) && (getInfo().getProjectManager().getFileAssociationsManager().getAssociatedEditors(FileUtils.getExtension(getCurrentFile())).size() > 1)) {
                    for (int i = 1; i < getInfo().getProjectManager().getFileAssociationsManager().getAssociatedEditors(FileUtils.getExtension(getCurrentFile())).size(); i++) {
                        final int editorID = getInfo().getProjectManager().getFileAssociationsManager().getAssociatedEditors(FileUtils.getExtension(getCurrentFile())).get(i);
                        ViewerEditor editor = getInfo().getEditor(editorID);
                        JMenuItem popupMenuOpenInEditor = new JMenuItem(editor.getPopupMenuText(), getInfo().getImage(editor.getIcon()));
                        popupMenuOpenInEditor.setMnemonic(editor.getPopupMenuMnemonic());
                        popupMenuOpenInEditor.setFont(plainFont);
                        popupMenuOpenInEditor.addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent arg0) {
                                openInEditorAction(editorID);
                            }
                        });
                        if ((editor instanceof NetlistViewerEditor) && (getInfo().getEditor(FileAssociationsManager.EDITOR_GRAPHIC_NETLIST)).getCurrentFile() == null) popupMenuOpenInEditor.setEnabled(false);
                        popupMenu.add(popupMenuOpenInEditor);
                    }
                }
                popupMenu.add(popupMenuNew);
                popupMenu.addSeparator();
                popupMenu.add(popupMenuCut);
                popupMenu.add(popupMenuCopy);
                popupMenu.add(popupMenuPaste);
                popupMenu.add(popupMenuDelete);
                popupMenu.add(popupMenuRename);
                popupMenu.addSeparator();
                popupMenu.add(popupMenuImport);
                popupMenu.add(popupMenuExport);
                if (isProjectSelected()) popupMenu.add(popupMenuExportAsZip);
                popupMenu.addSeparator();
                popupMenu.add(popupMenuRefresh);
                popupMenu.addSeparator();
                popupMenu.add(popupMenuProperties);
            } else {
                popupMenu.removeAll();
                popupMenu.add(popupMenuCut);
                popupMenu.add(popupMenuCopy);
                popupMenu.add(popupMenuPaste);
                popupMenu.add(popupMenuDelete);
                popupMenu.addSeparator();
                popupMenu.add(popupMenuExport);
                popupMenu.addSeparator();
                popupMenu.add(popupMenuRefresh);
            }
        } else {
            popupMenu.removeAll();
            popupMenu.add(popupMenuNew);
            popupMenu.add(popupMenuOpen);
            popupMenu.addSeparator();
            popupMenu.add(popupMenuRefresh);
        }
    }

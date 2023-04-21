    protected void handleEditButton() {
        final int selectedIndex = table.getSelectionIndex();
        if (selectedIndex == -1) {
            ControlFactory.showMessageDialog("Please verify an Available Namespace is selected to perform Edit operation.", "Information");
        } else {
            final AddNamespacesInfoDlg editDlg = new AddNamespacesInfoDlg(parent, "Edit Namespace", (ObjectPair) contentProvider.getRow(selectedIndex));
            final ObjectPair updatedColumn = editDlg.getUpdateRowData();
            final Object firstObj = updatedColumn.getFirstObject();
            if (firstObj != null) {
                final String updatedPrefix = firstObj.toString().trim();
                if (contentProvider.contains(updatedColumn)) {
                    final int oldIndex = contentProvider.findRow(updatedColumn);
                    if (oldIndex != selectedIndex) {
                        final int result = ControlFactory.showConfirmDialog("Namespace already exists, Do you want to Overwrite?");
                        if (result == Window.OK) {
                            contentProvider.updateRow(selectedIndex, updatedColumn, false);
                            contentProvider.removeRow(oldIndex);
                        }
                    } else {
                        contentProvider.updateSelectedRow(updatedColumn, false);
                    }
                } else if (!(updatedPrefix.equals("xa"))) {
                    contentProvider.updateRow(selectedIndex, updatedColumn, false);
                }
            }
        }
    }

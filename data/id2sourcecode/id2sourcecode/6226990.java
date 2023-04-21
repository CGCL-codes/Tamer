    public void widgetSelected(final SelectionEvent e) {
        int selectedIndex = table.getSelectionIndex();
        if (selectedIndex != -1 && ((ObjectPair) contentProvider.getRow(selectedIndex)).getFirstObject().toString().equals("xa")) {
            editBtn.setEnabled(false);
            deleteBtn.setEnabled(false);
        } else {
            editBtn.setEnabled(true);
            deleteBtn.setEnabled(true);
        }
        if (e.getSource() == addBtn) {
            selectedIndex = table.getItemCount() + 1;
            final AddNamespacesInfoDlg addDlg = new AddNamespacesInfoDlg(parent, "Add Namespace", new ObjectPair("", ""));
            final ObjectPair updatedColumn = addDlg.getUpdateRowData();
            final Object firstObj = updatedColumn.getFirstObject();
            if (firstObj != null) {
                final String updatedPrefix = firstObj.toString().trim();
                if (!(updatedPrefix.equals("xa"))) {
                    if (contentProvider.contains(updatedColumn)) {
                        final int result = ControlFactory.showConfirmDialog("Namespace already exists, Do you want to Overwrite?");
                        if (result == Window.OK) {
                            contentProvider.updateRow(contentProvider.findRow(updatedColumn), updatedColumn, false);
                        }
                    } else {
                        contentProvider.addRow(updatedColumn);
                    }
                }
            }
        }
        if (e.getSource() == editBtn) {
            handleEditButton();
        }
        if (e.getSource() == deleteBtn) {
            if (selectedIndex == -1) {
                ControlFactory.showMessageDialog("Please verify an Available Namespace is selected to perform Delete operation.", "Information");
            } else {
                contentProvider.removeRow(selectedIndex);
            }
        }
    }

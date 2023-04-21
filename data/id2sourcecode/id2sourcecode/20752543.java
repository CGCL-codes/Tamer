    private void btnMoveDownActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedIndex = lstData.getSelectedIndex();
        if (selectedIndex < lstData.getModel().getSize() - 1) {
            Object obj = ((DefaultListModel) lstData.getModel()).remove(selectedIndex);
            ((DefaultListModel) lstData.getModel()).insertElementAt(obj, selectedIndex + 1);
            lstData.setSelectedIndex(selectedIndex + 1);
            int tmp = indicesOrder[selectedIndex];
            indicesOrder[selectedIndex] = indicesOrder[selectedIndex + 1];
            indicesOrder[selectedIndex + 1] = tmp;
        }
    }

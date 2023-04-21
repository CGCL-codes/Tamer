    private void okBtActionPerformed(java.awt.event.ActionEvent evt) {
        if (readCb.isSelected() && writeCb.isSelected()) permission.setPermission(SvnEntityPermission.PERM_ALL); else if (readCb.isSelected()) {
            permission.setPermission(SvnEntityPermission.PERM_READ);
        } else if (writeCb.isSelected()) {
            permission.setPermission(SvnEntityPermission.PERM_WRITE);
        }
        ((SVNAdminView) SVNAdmin.getApplication().getMainView()).syncPermissionChanges(permission);
        ((SVNAdminView) SVNAdmin.getApplication().getMainView()).updatePermissionsLists();
        this.setVisible(false);
    }

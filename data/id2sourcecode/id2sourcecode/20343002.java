        @Override
        public void permissionChanged(User user) {
            if (user.isLocal()) {
                SarosView.showNotification(Messages.PermissionManager_permission_changed, MessageFormat.format(Messages.PermissionManager_you_have_now_access, user.getHumanReadableName(), user.hasWriteAccess() ? Messages.PermissionManager_write : Messages.PermissionManager_read_only));
            } else {
                SarosView.showNotification(Messages.PermissionManager_permission_changed, MessageFormat.format(Messages.PermissionManager_he_has_now_access, user.getHumanReadableName(), user.hasWriteAccess() ? Messages.PermissionManager_write : Messages.PermissionManager_read_only));
            }
        }

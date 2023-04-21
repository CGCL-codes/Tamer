    public boolean isUsable() {
        boolean disabled = isDisabled();
        boolean read = isReadOnly();
        boolean writePermissions = getEffectivePermission(SecurityPermissions.WRITE);
        boolean visible = isVisible();
        if (disabled || read || !writePermissions || !visible) {
            return false;
        }
        return true;
    }

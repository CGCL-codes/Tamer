    public boolean hasFullPermissions() {
        if (read && write && delete && share) return true;
        return false;
    }

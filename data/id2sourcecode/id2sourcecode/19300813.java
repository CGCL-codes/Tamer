    private boolean canPrintToFile() {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            try {
                sm.checkPermission(new FilePermission("<<ALL FILES>>", "read,write"));
                return true;
            } catch (SecurityException e) {
                return false;
            }
        }
        return true;
    }

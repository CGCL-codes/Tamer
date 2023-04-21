    private void throwPrintToFile() {
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            if (printToFilePermission == null) {
                printToFilePermission = new FilePermission("<<ALL FILES>>", "read,write");
            }
            security.checkPermission(printToFilePermission);
        }
    }

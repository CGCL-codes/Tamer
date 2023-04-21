    private static void renrenRequest() {
        if (!java.awt.Desktop.isDesktopSupported()) {
            System.err.println("Desktop is not supported (fatal)");
            System.exit(1);
        }
        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
        if (desktop == null || !desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
            System.err.println("Desktop doesn't support the browse action");
            System.exit(1);
        }
        testAPI();
    }

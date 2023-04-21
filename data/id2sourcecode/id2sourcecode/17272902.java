    public static void initGlobal(SecurityController controller) {
        if (controller == null) throw new IllegalArgumentException();
        if (global != null) {
            throw new SecurityException("Cannot overwrite already installed global SecurityController");
        }
        global = controller;
    }

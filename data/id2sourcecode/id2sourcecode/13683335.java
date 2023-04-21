    public static boolean usage(Screen screen, String cmd) {
        if (cmd.equals("/ls")) {
            screen.writeln("/ls : Displays the list of connected readers");
            return true;
        }
        return false;
    }

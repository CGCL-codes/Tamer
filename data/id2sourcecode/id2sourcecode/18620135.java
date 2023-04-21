    protected void handleUp(Log out, CommandManager cmdman) {
        if (currentCategory.equals("top")) {
            out.writeln(Bundle.getString("help.atTopAlready"));
        } else {
            int dot = currentCategory.lastIndexOf('.');
            currentCategory = currentCategory.substring(0, dot);
            printCategory(out, currentCategory);
        }
        cmdman.grabInput(this);
    }

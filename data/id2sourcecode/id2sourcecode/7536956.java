    private boolean overwriteTest(String name) throws IOException {
        if (containsScript(name)) {
            if (Dialog.show("Script Exists", "The script " + name + " already exists, do you want to overwrite it?", "Cancel", "Overwrite")) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkOverWrite(File path) {
        if (path.exists()) {
            return EncogWorkBench.askQuestion("Overwrite", "This file already exists.  Do you wish to overwrite it?");
        }
        return true;
    }

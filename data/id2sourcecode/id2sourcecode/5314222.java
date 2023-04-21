    public static final synchronized void revertFile(IKittenView kittenView, KittenProperties kittenProperties) {
        if (kittenView.getOpenedFile() != null && !kittenView.getOpenedFile().equals("")) {
            File selFile = new File(kittenView.getOpenedFile());
            if (selFile.exists()) {
                if (readFile(selFile) != null) {
                    kittenView.writeOpenFile2Screen(readFile(selFile));
                    kittenView.setOpenedFile(selFile.getPath());
                }
            } else {
                log.warning("Could not revert file '" + kittenView.getOpenedFile() + "' because it does not exist!");
            }
        }
    }

    private void backup(final NhapParams nhapParams) throws IOException {
        final Player player = representationManager.getPlayer();
        final File nethackDirectory = nhapParams.getNethackDirectory();
        final File savedFile = new File(nethackDirectory, nhapParams.getEnduser() + "-" + player.getName() + ".NetHack-saved-game");
        final File backupFile = new File(nethackDirectory, nhapParams.getEnduser() + "-" + player.getName() + ".NetHack-saved-game." + nhapParams.getTime());
        FileUtils.copyFile(savedFile, backupFile);
    }

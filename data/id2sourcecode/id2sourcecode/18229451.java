    public void checkpoint(File dir, List<RecoverAction> actions) throws IOException {
        int id = System.identityHashCode(this);
        String backup = "seeds" + id + " .txt";
        FileUtils.copyFile(getSeedfile(), new File(dir, backup));
        actions.add(new SeedModuleRecoverAction(backup, getSeedfile()));
    }

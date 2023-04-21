    protected void copyFiles(List<String> files2Copy, Writer protocol) throws IOException {
        for (String curFile : files2Copy) {
            File srcFile = new File(curFile);
            File target = new File(getConfig().getTargetDir(), curFile);
            getModel().setMessage(new StringBuffer("install file ").append(curFile).toString());
            if (getConfig().isSudoTarget()) {
                if (!FileUtils.copyFile(target, srcFile, getConfig().getNixUser())) throw new ApplicationException("failed to copy " + curFile);
            } else {
                if (!FileUtils.copyFile(target, srcFile)) throw new ApplicationException("failed to copy " + curFile);
            }
            protocol.write(target.getAbsolutePath());
            protocol.write("\n");
        }
    }

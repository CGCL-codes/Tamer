    @Override
    public void renamePromptFile(int promptId, String projectName, String name, String extension) throws Exception {
        Prompt p = DBManager.getInstance().getPromptByID(promptId);
        if (p == null) throw new DBManagerException("Couldn't find prompt with id " + promptId);
        String oldName = "safi" + File.separatorChar + (p.getProject() == null ? "shared" + File.separatorChar + p.getName() + "." + p.getExtension() : ("project" + File.separatorChar + (p.getProject().getName() + File.separatorChar + p.getName() + "." + p.getExtension())));
        String newName = "safi" + File.separatorChar + (projectName == null ? "shared" + File.separatorChar + name + "." + extension : ("project" + File.separatorChar + (projectName + File.separatorChar + name + "." + extension)));
        String oldNameFull = SafletEngine.getInstance().getAudioDirectoryRoot() + File.separatorChar + oldName;
        String newNameFull = SafletEngine.getInstance().getAudioDirectoryRoot() + File.separatorChar + newName;
        if (StringUtils.equals(oldNameFull, newNameFull)) {
            log.info("Prompt path was unchanged: " + oldNameFull);
            return;
        }
        File newFile = new File(newNameFull);
        File parent = newFile.getParentFile();
        if (!parent.exists() && !parent.mkdirs()) throw new IOException("Couldn't create directory path: " + parent);
        File oldFile = new File(oldNameFull);
        if (newFile.equals(oldFile)) return;
        if (parent.equals(newFile.getParent())) {
            if (!oldFile.renameTo(newFile)) {
                throw new IOException("Couldn't rename " + oldFile + " to " + newFile);
            }
        } else {
            FileUtils.copyFile(oldFile, newFile);
            FileUtils.deleteFileAndEmptyParents(oldFile);
        }
    }

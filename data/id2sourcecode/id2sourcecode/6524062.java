    @Override
    public void doSave(Project project, File file) throws SaveException, InterruptedException {
        LOG.info("Saving");
        ProgressMgr progressMgr = new ProgressMgr();
        progressMgr.setNumberOfPhases(4);
        progressMgr.nextPhase();
        File lastArchiveFile = new File(file.getAbsolutePath() + "~");
        File tempFile = null;
        try {
            tempFile = createTempFile(file);
        } catch (FileNotFoundException e) {
            throw new SaveException("Failed to archive the previous file version", e);
        } catch (IOException e) {
            throw new SaveException("Failed to archive the previous file version", e);
        }
        BufferedWriter writer = null;
        try {
            project.setFile(file);
            project.setVersion(ApplicationVersion.getVersion());
            project.setPersistenceVersion(PERSISTENCE_VERSION);
            ZipOutputStream stream = new ZipOutputStream(new FileOutputStream(file));
            writer = new BufferedWriter(new OutputStreamWriter(stream, "UTF-8"));
            for (ProjectMember projectMember : project.getMembers()) {
                if (projectMember.getType().equalsIgnoreCase("xmi")) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Saving member of type: " + projectMember.getType());
                    }
                    stream.putNextEntry(new ZipEntry(projectMember.getZipName()));
                    MemberFilePersister persister = getMemberFilePersister(projectMember);
                    persister.save(projectMember, writer);
                }
            }
            if (lastArchiveFile.exists()) {
                lastArchiveFile.delete();
            }
            if (tempFile.exists() && !lastArchiveFile.exists()) {
                tempFile.renameTo(lastArchiveFile);
            }
            if (tempFile.exists()) {
                tempFile.delete();
            }
            progressMgr.nextPhase();
        } catch (Exception e) {
            LOG.error("Exception occured during save attempt", e);
            try {
                writer.close();
            } catch (Exception ex) {
            }
            file.delete();
            tempFile.renameTo(file);
            throw new SaveException(e);
        }
        try {
            writer.close();
        } catch (IOException ex) {
            LOG.error("Failed to close save output writer", ex);
        }
    }

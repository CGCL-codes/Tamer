    public void doSave(Project project, File file) throws SaveException {
        LOG.info("Receiving file '" + file.getName() + "'");
        File lastArchiveFile = new File(file.getAbsolutePath() + "~");
        File tempFile = null;
        try {
            tempFile = createTempFile(file);
        } catch (FileNotFoundException e) {
            throw new SaveException("Failed to archive the previous file version", e);
        } catch (IOException e) {
            throw new SaveException("Failed to archive the previous file version", e);
        }
        OutputStream bufferedStream = null;
        try {
            ZipOutputStream stream = new ZipOutputStream(new FileOutputStream(file));
            String fileName = file.getName();
            ZipEntry xmiEntry = new ZipEntry(fileName.substring(0, fileName.lastIndexOf(".")));
            stream.putNextEntry(xmiEntry);
            bufferedStream = new BufferedOutputStream(stream);
            int size = project.getMembers().size();
            for (int i = 0; i < size; i++) {
                ProjectMember projectMember = project.getMembers().get(i);
                if (projectMember.getType().equalsIgnoreCase("xmi")) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Saving member of type: " + (project.getMembers().get(i)).getType());
                    }
                    MemberFilePersister persister = new ModelMemberFilePersister();
                    persister.save(projectMember, bufferedStream);
                }
            }
            stream.close();
            if (lastArchiveFile.exists()) {
                lastArchiveFile.delete();
            }
            if (tempFile.exists() && !lastArchiveFile.exists()) {
                tempFile.renameTo(lastArchiveFile);
            }
            if (tempFile.exists()) {
                tempFile.delete();
            }
        } catch (Exception e) {
            LOG.error("Exception occured during save attempt", e);
            try {
                bufferedStream.close();
            } catch (IOException ex) {
            }
            file.delete();
            tempFile.renameTo(file);
            throw new SaveException(e);
        }
        try {
            bufferedStream.close();
        } catch (IOException ex) {
            LOG.error("Failed to close save output writer", ex);
        }
    }

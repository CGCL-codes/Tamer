    private Integer copyResourceToRepo(File source, @Nullable Integer checklistId) throws ChecklistImportError {
        File sourceFolder = source;
        if (checklistId == null) {
            checklistId = generateId();
        }
        if (org.gbif.utils.file.FileUtils.isCompressedFile(source)) {
            try {
                sourceFolder = File.createTempFile(checklistId.toString(), source.getName());
                if (!(sourceFolder.delete())) {
                    throw new IOException("Could not delete temp file: " + sourceFolder.getAbsolutePath());
                }
                if (!(sourceFolder.mkdir())) {
                    throw new IOException("Could not create temp directory: " + sourceFolder.getAbsolutePath());
                }
                log.debug("DECOMPRESS ARCHIVE " + source.getAbsolutePath());
                CompressionUtil.decompressFile(sourceFolder, source);
            } catch (IOException e) {
                throw new ChecklistImportError("Cannot decompress dwc archive file " + source.getAbsolutePath());
            }
        }
        try {
            Archive archive = ArchiveFactory.openArchive(sourceFolder);
        } catch (Exception e) {
            throw new ChecklistImportError("Cannot open dwc archive", e);
        }
        try {
            File dwcaRepoDir = cfg.importDir(checklistId);
            if (dwcaRepoDir.exists()) {
                log.debug("clear existing source folder " + dwcaRepoDir.getAbsolutePath());
                FileUtils.deleteDirectory(dwcaRepoDir);
            }
            log.debug("copy checklist " + sourceFolder.getName() + " to repo: " + dwcaRepoDir.getAbsolutePath());
            if (sourceFolder.isFile()) {
                org.apache.commons.io.FileUtils.copyFileToDirectory(sourceFolder, dwcaRepoDir);
            } else {
                org.apache.commons.io.FileUtils.copyDirectory(sourceFolder, dwcaRepoDir);
            }
        } catch (IOException e) {
            throw new ChecklistImportError("Cannot copy resource", e);
        }
        return checklistId;
    }

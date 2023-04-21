    private void updateSources(Checklist chkl) throws ChecklistImportError {
        boolean updated = false;
        URL url = chkl.getDownloadUriAsUrl();
        if (url == null) {
            log.warn("Checklist " + chkl.getId() + " missing download url");
            return;
        } else {
            File localDwca = url2file(url);
            if (localDwca == null) {
                localDwca = cfg.tmpFile(url);
                try {
                    updated = http.downloadIfChanged(url, chkl.getLastDownload(), localDwca);
                } catch (IOException e1) {
                    log.error("URL cant be reached: " + url, e1);
                    throw new ChecklistImportError("Cannot download dwc archive file from " + url);
                }
            } else {
                updated = true;
            }
            if (!updated || !localDwca.exists()) {
                log.info("Source files for checklist " + chkl.getId() + " already up to date");
            } else {
                File sourceFolder = localDwca;
                try {
                    File sourceFolderTmp = FileUtils.createTempDir(String.format("%04d", chkl.getId()), localDwca.getName());
                    log.debug("Decompressing archive " + localDwca.getAbsolutePath());
                    CompressionUtil.decompressFile(sourceFolderTmp, localDwca);
                    sourceFolder = sourceFolderTmp;
                } catch (UnsupportedCompressionType e) {
                    log.warn("Failed to decompress file " + localDwca.getAbsolutePath() + ". Maybe this is an uncompressed text file, lets try", e);
                } catch (IOException e) {
                    throw new ChecklistImportError("Cannot decompress dwc archive file " + localDwca.getAbsolutePath(), e);
                }
                try {
                    Archive archive = ArchiveFactory.openArchive(sourceFolder);
                } catch (Exception e) {
                    throw new ChecklistImportError("Cannot open dwc archive. Keep existing source files", e);
                }
                try {
                    File dwcaRepoDir = cfg.importDir(chkl.getId());
                    if (dwcaRepoDir.exists()) {
                        log.debug("clear existing source folder " + dwcaRepoDir.getAbsolutePath());
                        org.apache.commons.io.FileUtils.deleteDirectory(dwcaRepoDir);
                    }
                    log.debug("copy checklist " + sourceFolder.getName() + " to repo: " + dwcaRepoDir.getAbsolutePath());
                    if (sourceFolder.isFile()) {
                        org.apache.commons.io.FileUtils.copyFileToDirectory(sourceFolder, dwcaRepoDir);
                    } else {
                        org.apache.commons.io.FileUtils.copyDirectory(sourceFolder, dwcaRepoDir);
                    }
                } catch (IOException e) {
                    throw new ChecklistImportError("Failed to copy source files", e);
                }
                chkl.setLastDownload(new Date());
                checklistService.update(chkl);
            }
        }
    }

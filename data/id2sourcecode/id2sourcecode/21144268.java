    public void updateDwcaEml(Resource resource, BaseAction action) throws PublicationException {
        ActionLogger alog = new ActionLogger(this.log, action);
        if (isLocked(resource.getShortname())) {
            throw new PublicationException(PublicationException.TYPE.LOCKED, "Resource " + resource.getShortname() + " is currently locked by another process");
        }
        if (!resource.hasPublishedData()) {
            throw new PublicationException(PublicationException.TYPE.DWCA, "Resource " + resource.getShortname() + " has no published data - can't update a non-existent dwca.");
        }
        try {
            File dwcaFolder = dataDir.tmpDir();
            if (log.isDebugEnabled()) {
                log.debug("Using tmp dir [" + dwcaFolder.getAbsolutePath() + "]");
            }
            File dwcaFile = dataDir.resourceDwcaFile(resource.getShortname());
            if (log.isDebugEnabled()) {
                log.debug("Using dwca file [" + dwcaFile.getAbsolutePath() + "]");
            }
            File emlFile = dataDir.resourceEmlFile(resource.getShortname(), resource.getEmlVersion());
            if (log.isDebugEnabled()) {
                log.debug("Using eml file [" + emlFile.getAbsolutePath() + "]");
            }
            CompressionUtil.unzipFile(dwcaFolder, dwcaFile);
            if (log.isDebugEnabled()) {
                log.debug("Copying new eml file [" + emlFile.getAbsolutePath() + "] to [" + dwcaFolder.getAbsolutePath() + "] as eml.xml");
            }
            FileUtils.copyFile(emlFile, new File(dwcaFolder, "eml.xml"));
            File zip = dataDir.tmpFile("dwca", ".zip");
            CompressionUtil.zipDir(dwcaFolder, zip);
            dwcaFile.delete();
            FileUtils.moveFile(zip, dwcaFile);
        } catch (IOException e) {
            alog.error("Can't update dwca for resource " + resource.getShortname(), e);
            throw new PublicationException(PublicationException.TYPE.DWCA, "Could not process dwca file for resource [" + resource.getShortname() + "]");
        }
    }

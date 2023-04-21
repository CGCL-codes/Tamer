    public void publishMetadata(Resource resource, BaseAction action) throws PublicationException {
        ActionLogger alog = new ActionLogger(this.log, action);
        if (isLocked(resource.getShortname())) {
            throw new PublicationException(PublicationException.TYPE.LOCKED, "Resource " + resource.getShortname() + " is currently locked by another process");
        }
        int version = resource.getEmlVersion();
        version++;
        resource.setEmlVersion(version);
        saveEml(resource);
        File trunkFile = dataDir.resourceEmlFile(resource.getShortname(), null);
        File versionedFile = dataDir.resourceEmlFile(resource.getShortname(), version);
        try {
            FileUtils.copyFile(trunkFile, versionedFile);
        } catch (IOException e) {
            alog.error("Can't publish resource " + resource.getShortname(), e);
            throw new PublicationException(PublicationException.TYPE.EML, "Can't publish eml file for resource " + resource.getShortname(), e);
        }
        publishRtf(resource, action);
        File trunkRtfFile = dataDir.resourceRtfFile(resource.getShortname());
        File versionedRtfFile = dataDir.resourceRtfFile(resource.getShortname(), version);
        try {
            FileUtils.copyFile(trunkRtfFile, versionedRtfFile);
        } catch (IOException e) {
            alog.error("Can't publish resource " + resource.getShortname() + "as RTF", e);
            throw new PublicationException(PublicationException.TYPE.EML, "Can't publish rtf file for resource " + resource.getShortname(), e);
        }
    }

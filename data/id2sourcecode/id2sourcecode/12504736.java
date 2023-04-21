    public OriginalDeployUnitDescription saveFileToRepository(File file, String type, String target, String provider, String url) throws UnitRepositoryException {
        if (StringUtils.isEmpty(target)) target = COMMON;
        if (StringUtils.isEmpty(type)) type = COMMON;
        if (file == null || !file.exists()) {
            String m = String.format("The file [%s] don't exsit!", file.getAbsoluteFile());
            logger.error(m);
            throw new UnitRepositoryException(m);
        }
        String targetFileString = this.baseUrl + File.separator + type + File.separator + target;
        try {
            FileUtils.copyFileToDirectory(file, new File(targetFileString), true);
        } catch (IOException e) {
            String m = String.format("Can't move [%s] to [%s]", file.getAbsoluteFile(), targetFileString);
            logger.error(m);
            throw new UnitRepositoryException(m, e);
        }
        OriginalDeployUnitDescription ouD = new OriginalDeployUnitDescription(url);
        ouD.setDate(Calendar.getInstance().getTime());
        ouD.setUrl(url);
        ouD.setFilename(file.getName());
        ouD.setStatus(OriginalDeployUnitDescription.INVALIDATED);
        ouD.setRepositoryUrl(this.baseUrl);
        ouD.setType(type);
        ouD.setTarget(target);
        ouD.setProvider(provider);
        ouD.save();
        return ouD;
    }

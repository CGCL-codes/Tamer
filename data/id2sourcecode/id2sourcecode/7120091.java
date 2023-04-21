    public static ModuleResource getModuleResource(NameVersion moduleName, int type) {
        try {
            URI moduleURI = new URI(moduleName.name);
            String s = moduleURI.getScheme();
            if (s == null || s.equals("jiopi")) {
                return ResourcePool.getModuleResource(moduleName, type);
            } else {
                String fileName = FileUtil.getFileName(moduleName.name, false);
                String moduleNameHash = fileName + Long.toHexString(MD5Hash.digest(moduleName.name).halfDigest());
                if (logger.isDebugEnabled()) logger.debug("get single module " + moduleNameHash);
                String moduleProgramPath = FileUtil.joinPath(ResourceUtil.getProgramDir(), KernelConstants.MODULES_DIR, moduleNameHash);
                ModuleConfig moduleConfig = getModuleConfig(moduleNameHash, moduleURI.toURL(), moduleProgramPath, false, null);
                ModuleConfig.Release release = moduleConfig.getRelease(moduleName);
                if (release == null) return null;
                logger.info(moduleName.name + " get Version :" + moduleName.version + " use Version :" + release.version);
                URL[] releaseResources = release.getResources();
                if (releaseResources != null) {
                    return new ModuleResource(new NameVersion(moduleNameHash, release.version), releaseResources, type);
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }

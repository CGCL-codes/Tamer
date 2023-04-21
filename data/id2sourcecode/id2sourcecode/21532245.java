    static void upload(File tmp, String scmUrl, Set<DistroType> what) throws IOException, InterruptedException {
        final File codeDir = new File(tmp, "code");
        final RevisionControl vcs = RevisionControl.Factory.get(scmUrl);
        AbstractStrictLogger.injectLogger(vcs, logger.getSubLogger("vcs"));
        logger.log(cat.checkout(scmUrl));
        vcs.checkout(scmUrl, codeDir);
        final File configFile = new File(StringUtils.expandSysProps("${user.home}/src/buildbox/tools/releasator/data/releasator-config.xml"));
        FileUtils.copyFile(configFile, new File(tmp, configFile.getName()), true);
        final CodeBuilder bs = new MavenBuilderFactory().get(codeDir, tmp);
        final Map<DistroType, Dray> drays = new LinkedHashMap<DistroType, Dray>();
        for (DistroType distroType : what) {
            final Dray dray = config.getUploadDray(bs.getGroupId(), bs.getArtifactId(), bs.getVersion(), distroType);
            AbstractStrictLogger.injectLogger(dray, logger.getSubLogger("dray." + distroType));
            drays.put(distroType, dray);
        }
        AbstractStrictLogger.injectLogger(bs, logger.getSubLogger("bs"));
        logger.log(cat.building(bs.getGroupId(), bs.getArtifactId(), bs.getVersion()));
        for (DistroType distroType : what) {
            bs.requestDistro(distroType);
            logger.log(cat.reqDistro(distroType));
        }
        final BuildResult buildResult = bs.build();
        for (DistroType distroType : what) {
            final File d = buildResult.getDistro(distroType);
            logger.log(cat.uploading(distroType));
            final Dray dray = drays.get(distroType);
            dray.uploadDirectory(d, ".");
        }
    }

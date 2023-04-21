    @Override
    public void run() {
        logger.debug("Entered run.");
        File siteDir = PoPathInfo.getSiteDirectory(this.site);
        if (monitor != null) monitor.beginTask(String.format("%s: %d", LabelHolder.get("task.export.monitor"), this.renderableObjects.size()), this.renderableObjects.size());
        if (CollectionUtils.isEmpty(site.getPages())) renderRedirector();
        try {
            FileUtils.cleanDirectory(exportDir);
            for (IRenderable ro : renderableObjects) {
                File dir = PathTool.getExportFile(ro, poExtension).getParentFile();
                if (!dir.exists()) dir.mkdirs();
            }
            renderRenderables();
            if (!exportController.isError() && !isInterruptByUser) {
                logger.debug("Static export successfull!");
                Set<File> filesToCopy = renderData.getFilesToCopy();
                for (File srcFile : filesToCopy) {
                    String exportPathPart = srcFile.getAbsolutePath().substring(siteDir.getAbsolutePath().length() + 1);
                    File destFile = new File(exportDir, exportPathPart);
                    if (srcFile.isFile()) FileUtils.copyFile(srcFile, destFile); else FileUtils.copyDirectoryToDirectory(srcFile, destFile.getParentFile());
                }
                logger.debug("Extra files successful copied!");
                Collection<File> exportedFiles = FileTool.collectFiles(exportDir);
                if (monitor != null) {
                    monitor.done();
                    monitor.beginTask("Calculate checksums", exportedFiles.size());
                }
                Document dom = ChecksumTool.getDomChecksums(ChecksumTool.get(exportedFiles, exportDir.getAbsolutePath(), monitor));
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                OutputFormat outformat = OutputFormat.createPrettyPrint();
                outformat.setEncoding(Constants.STANDARD_ENCODING);
                XMLWriter writer = new XMLWriter(out, outformat);
                writer.write(dom);
                writer.flush();
                String formatedDomString = out.toString();
                InputStream in = new ByteArrayInputStream(formatedDomString.getBytes());
                Map<InputStream, String> toCompress = new HashMap<InputStream, String>();
                toCompress.put(in, checksumFilename);
                File zipFile = new File(PoPathInfo.getSiteExportDirectory(site), FilenameUtils.getBaseName(checksumFilename) + ".zip");
                Zip.compress(zipFile, toCompress);
                zipFile = null;
            } else FileUtils.cleanDirectory(exportDir);
        } catch (Exception e) {
            logger.error("Error while export: " + e.getMessage(), e);
            throw new FatalException("Error while export " + this.site.getUrl() + e.getMessage(), e);
        } finally {
            if (monitor != null) monitor.done();
        }
    }

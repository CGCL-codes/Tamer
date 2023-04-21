    public void run() {
        logger.debug("Entered run.");
        File siteDir = new File(PojoPathInfo.getSitePath(this.site)).getAbsoluteFile();
        if (monitor != null) monitor.beginTask(LabelHolder.get("task.export.monitor") + this.renderableObjects.size(), this.renderableObjects.size());
        if (CollectionUtils.isEmpty(site.getPages())) renderRedirector();
        try {
            renderRenderables();
            if (!controller.isError() && !isInterruptByUser) {
                logger.debug("Static export successfull!");
                Collection<File> exportedFiles = FileTool.collectFiles(exportDir);
                if (monitor != null) {
                    monitor.done();
                    monitor.beginTask("Calculate checksums", exportedFiles.size());
                }
                Document dom = ChecksumTool.getDomChecksums(ChecksumTool.get(exportedFiles, exportDir.getAbsolutePath(), this.monitor));
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                OutputFormat outformat = OutputFormat.createPrettyPrint();
                outformat.setEncoding(Constants.XML_ENCODING);
                XMLWriter writer = new XMLWriter(out, outformat);
                writer.write(dom);
                writer.flush();
                String formatedDomString = out.toString();
                InputStream in = new ByteArrayInputStream(formatedDomString.getBytes());
                Map<InputStream, String> toCompress = new HashMap<InputStream, String>();
                toCompress.put(in, InitializationManager.getProperty("poormans.filename.checksums"));
                File zipFile = new File(PojoPathInfo.getSiteExportPath(site), FilenameUtils.getBaseName(InitializationManager.getProperty("poormans.filename.checksums")) + ".zip");
                Zip.compress(zipFile, toCompress);
                zipFile = null;
                Set<File> filesToCopy = renderData.getFilesToCopy();
                for (File srcFile : filesToCopy) {
                    String exportPathPart = srcFile.getAbsolutePath().substring(siteDir.getAbsolutePath().length() + 1);
                    File destFile = new File(exportDir, exportPathPart);
                    if (srcFile.isFile()) FileUtils.copyFile(srcFile, destFile); else FileUtils.copyDirectoryToDirectory(srcFile, destFile.getParentFile());
                }
            } else FileUtils.cleanDirectory(exportDir);
        } catch (Exception e) {
            logger.error("Error while export: " + e.getMessage(), e);
            throw new FatalException("Error while export " + this.site.getUrl() + e.getMessage(), e);
        } finally {
            if (monitor != null) monitor.done();
        }
    }

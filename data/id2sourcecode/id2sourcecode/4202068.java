    public static void generateAll(TemplateFactory factory, String templateRoot, String outputRoot, Map<String, Object> context, boolean overwrite, FileMapping fileMapping, String updatingFolder) throws Exception {
        String newTplRoot = StringUtils.cleanDirPath(templateRoot);
        factory.setResourceLoader(new FileSystemResourceLoader(newTplRoot));
        File root = new File(newTplRoot);
        List<File> files = new ArrayList<File>();
        FileHelper.listFiles(root, files);
        for (File f : files) {
            String relativePath = FileHelper.getRelativePath(root, f);
            String fileName = StringUtils.cleanPath(f.getPath());
            log.info("fileName:" + fileName);
            String targetFileName = fileMapping.getTargetFileName(relativePath, outputRoot, context);
            if (targetFileName == null || !targetFileName.startsWith(updatingFolder)) continue;
            File target = new File(targetFileName);
            if (f.isDirectory()) {
                log.info("make dir: " + targetFileName);
                target.mkdirs();
            } else if (FileHelper.isTemplate(fileName)) {
                String insertBlock = FileHelper.getInsertBlock(fileName);
                if (StringUtils.hasText(insertBlock)) {
                    log.info("insertblock:" + insertBlock);
                    String toInsert = parseFile(factory, relativePath, context);
                    try {
                        String src = FileUtils.readFileToString(target, "utf-8");
                        String newText = StringUtils.replace(src, insertBlock, toInsert);
                        log.info("...insert into: " + targetFileName);
                        FileUtils.writeStringToFile(target, newText, "utf-8");
                        log.info("...done!\n");
                    } catch (FileNotFoundException e) {
                        log.warn(e);
                        FileUtils.writeStringToFile(new File(targetFileName + ".insert"), toInsert, "utf-8");
                    }
                } else {
                    if (target.exists() && !overwrite) {
                        log.info("ignore exists file:" + targetFileName);
                        continue;
                    }
                    String text = parseFile(factory, relativePath, context);
                    log.info("...write file to: " + targetFileName);
                    FileUtils.writeStringToFile(target, text, "utf-8");
                    log.info("...done!\n");
                }
            } else if (target.exists() && !overwrite) {
                log.info("ignore exists file:" + targetFileName);
                continue;
            } else {
                log.info("copying file: " + relativePath);
                FileUtils.copyFile(f, target);
                log.info("...done!\n");
            }
        }
    }

    public void commit(CodeGenContext ctx) throws Exception {
        List<FileContents> tempFileContents = ctx.getTempFileContents();
        if (tempFileContents != null) {
            for (Iterator<FileContents> iterator = tempFileContents.iterator(); iterator.hasNext(); ) {
                FileContents fileContent = iterator.next();
                logger.debug("FileContents to write/update is " + fileContent);
                int operation = fileContent.getOperation();
                final String path = fileContent.getFilePath();
                if (path == null) {
                    throw new IllegalStateException("File path is null...unexpected for " + fileContent);
                }
                final String dirPath = path.substring(0, path.lastIndexOf("/"));
                Object content = fileContent.getFileContents();
                File fileToWriteContentsInto = new File(path);
                String fileName = fileToWriteContentsInto.getName();
                switch(operation) {
                    case FileContents.OPERATION_CREATE:
                        if (fileToWriteContentsInto.exists()) {
                            String message = "File already exists, cannot write. Please check the path : " + path;
                            fileContent.setErrorMessage(message);
                            logger.error(message);
                        }
                        File dir = new File(dirPath);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        if (!(fileName.toLowerCase().endsWith(".xml"))) {
                            FileUtils.writeStringToFile(fileToWriteContentsInto, content.toString());
                        } else {
                            Serializer serializer = new Serializer(new FileOutputStream(path));
                            serializer.setIndent(4);
                            serializer.write((Document) content);
                        }
                        break;
                    case FileContents.OPERATION_UPDATE:
                        if (fileToWriteContentsInto.exists()) {
                            Date date = new Date(System.currentTimeMillis());
                            CodeGenUtil.cleanBackupFiles(dirPath);
                            fileToWriteContentsInto.renameTo(new File(path + CodeGenConstants.BKP_PREFIX + new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss").format(date)));
                        }
                        if (!(fileName.toLowerCase().endsWith(".xml"))) {
                            FileUtils.writeStringToFile(fileToWriteContentsInto, content.toString());
                        } else {
                            Serializer serializer = new Serializer(new FileOutputStream(path));
                            serializer.setIndent(4);
                            serializer.write((Document) content);
                            String cleanedUpDirtyAttributes = FileUtils.readFileToString(fileToWriteContentsInto).replaceAll(" xmlns=\"\"", "");
                            FileUtils.writeStringToFile(fileToWriteContentsInto, cleanedUpDirtyAttributes);
                        }
                        break;
                    default:
                        fileContent.setErrorMessage("File operation is not provided.");
                        logger.error("File operation is not provided.");
                }
            }
        }
    }

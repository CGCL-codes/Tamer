    public void process(boolean preview) {
        if (!configuration.getSrcDir().exists()) throw new IllegalStateException(SRC_DIR_NOT_FOUND_ERR_MSG);
        if (!configuration.getSrcDir().isDirectory()) throw new IllegalStateException(SRC_DIR_NOT_DIR_ERR_MSG);
        long t = 0;
        if (logger.isInfoEnabled()) {
            t = System.currentTimeMillis();
            String msg = null;
            if (preview) msg = MessageFormat.format(WORK_IN_PROGRESS, PREVIEW_MODE); else MessageFormat.format(WORK_IN_PROGRESS, "");
            logger.info(msg);
        }
        if (sortedFiles.isEmpty()) {
            regexFileFilter = new RegexFileFilter(this.configuration.getFilterExpression());
            keyWordsPattern = new KeywordsPattern();
            jsPattern = new JsPattern(this.configuration.getPatternJsExpression());
            sortedFiles = new ArrayList<SortedFile>();
            processDir(configuration.getSrcDir());
        }
        if (!preview) {
            for (final SortedFile sortedFile : sortedFiles) {
                if (cancel) break;
                final File srcFile = sortedFile.getSrcFile();
                final File dstFile = sortedFile.getDstFile();
                if (StringUtils.isNotBlank(sortedFile.getErrorMsg())) {
                    rejectFile(sortedFile, false);
                } else if (dstFile != null) {
                    try {
                        setCurrentFile(srcFile);
                        boolean isRotated = false;
                        if (configuration.isRotate()) {
                            if ((configuration.getProcessAction() == ProcessAction.COPY) || (configuration.getProcessAction() == ProcessAction.MOVE)) {
                                final JpegFile jpegFile = new JpegFile(srcFile);
                                final ExifData exifData = jpegFile.getExifData();
                                if ((exifData != null) && ExifUtils.needsRotation(exifData.getOrientation())) {
                                    if (logger.isDebugEnabled()) logger.debug("Rotation : " + srcFile + " -> " + dstFile);
                                    final byte[] srcFileData = FileUtils.readFileToByteArray(srcFile);
                                    dstFile.getParentFile().mkdirs();
                                    final FileOutputStream fos = new FileOutputStream(dstFile.getAbsolutePath());
                                    fos.write(ExifUtils.getRotatedImage(srcFileData, exifData.getOrientation()));
                                    fos.close();
                                    if (configuration.getProcessAction() == ProcessAction.MOVE) {
                                        if (logger.isDebugEnabled()) logger.debug(" + Move => FileUtils.deleteQuietly : " + srcFile);
                                        FileUtils.deleteQuietly(srcFile);
                                    }
                                    isRotated = true;
                                }
                            }
                        }
                        if (!isRotated) {
                            switch(configuration.getProcessAction()) {
                                case COPY:
                                    if (logger.isDebugEnabled()) logger.debug("FileUtils.copyFile : " + srcFile + " -> " + dstFile);
                                    FileUtils.copyFile(srcFile, dstFile);
                                    break;
                                case MOVE:
                                    if (logger.isDebugEnabled()) logger.debug("FileUtils.moveFile : " + srcFile + " -> " + dstFile);
                                    FileUtils.deleteQuietly(dstFile);
                                    FileUtils.moveFile(srcFile, dstFile);
                                    break;
                                default:
                                    final String msg = MessageFormat.format(UNSUPPORTED_PROCESS_ACTION, configuration.getProcessAction());
                                    sortedFile.setErrorMsg(msg);
                                    logger.error(msg);
                                    throw new IllegalArgumentException(msg);
                            }
                        }
                    } catch (final IOException e) {
                        final StringBuilder msg = new StringBuilder("IOException : ").append(e.getMessage());
                        if (e.getCause() != null) msg.append(e.getCause());
                        logger.error(msg.toString(), e);
                        sortedFile.setErrorMsg(e.getMessage());
                        if (e.getCause() != null) sortedFile.setErrorCause(e.getCause().getMessage());
                        rejectFile(sortedFile, false);
                    }
                }
            }
        }
        if (logger.isInfoEnabled()) logger.info("Traitement en " + (System.currentTimeMillis() - t) + " ms");
    }

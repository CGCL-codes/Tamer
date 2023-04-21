    private File rejectFile(final SortedFile sortedFile, boolean preview) {
        final File srcFile = sortedFile.getSrcFile();
        File rejectDstFile = null;
        if (configuration.isRejectProcess()) {
            if (srcFile.isFile()) {
                if (logger.isDebugEnabled()) logger.debug("rejectFile : " + srcFile.getAbsolutePath());
                try {
                    final JpegFile jpegFile = new JpegFile(srcFile);
                    final ExifData exifData = jpegFile.getExifData();
                    final String targetFileName = keyWordsPattern.applyPattern(srcFile, configuration.getRejectPatternKeywordsExpression(), exifData);
                    final StringBuilder targetPath = new StringBuilder(configuration.getRejectDir().getAbsolutePath()).append(File.separator).append(targetFileName);
                    rejectDstFile = new File(targetPath.toString());
                    if (logger.isDebugEnabled()) logger.debug(" -> " + rejectDstFile.getAbsolutePath());
                    if (!preview) {
                        if (!(rejectDstFile.exists() && !configuration.isOverwrite())) {
                            switch(configuration.getProcessAction()) {
                                case COPY:
                                    logger.info(MessageFormat.format(REJECT_FILE_COPY, srcFile.getAbsolutePath(), rejectDstFile.getAbsolutePath()));
                                    FileUtils.copyFile(srcFile, rejectDstFile);
                                    break;
                                case MOVE:
                                    logger.info(MessageFormat.format(REJECT_FILE_MOVE, srcFile.getAbsolutePath(), rejectDstFile.getAbsolutePath()));
                                    FileUtils.deleteQuietly(rejectDstFile);
                                    FileUtils.moveFile(srcFile, rejectDstFile);
                                    break;
                                default:
                                    final String msg = MessageFormat.format(UNSUPPORTED_PROCESS_ACTION, configuration.getProcessAction());
                                    logger.error(msg);
                                    throw new IllegalArgumentException(msg);
                            }
                        } else {
                            logger.info(REJECT_FILE_EXISTS_ERR_MSG);
                        }
                    } else {
                        logger.debug(REJECT_FILE_IS_PREVIEW_ERR_MSG);
                    }
                } catch (final Exception ex) {
                    final String msg = MessageFormat.format(REJECT_FILE_ERR_MSG, ex.getMessage());
                    logger.error(msg, ex);
                }
            } else {
                logger.debug(REJECT_FILE_SRC_FILE_NOT_FILE_ERR_MSG);
            }
        } else {
            logger.trace(REJECT_FILE_DEACTIVATED_ERR_MSG);
        }
        return rejectDstFile;
    }

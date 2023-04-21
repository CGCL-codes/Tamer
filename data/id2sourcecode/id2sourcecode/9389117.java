    @Override
    public void execute(PluginContext ctx, Edition edition, OutletEditionAction action) {
        this.pluginCtx = ctx;
        this.actionInstance = action;
        this.instanceProperties = action.getPropertiesAsMap();
        String outputLocation;
        if (!isPropertySet(Property.OUTPUT_LOCATION)) {
            log(LogSeverity.SEVERE, "LOG_OUTPUT_LOCATION_MISSING");
            return;
        } else {
            outputLocation = getProperty(Property.OUTPUT_LOCATION);
            Map<String, Object> templateAttributes = new HashMap<String, Object>();
            templateAttributes.put(TEMPLATE_TAG_EDITION, edition);
            outputLocation = compileTemplate(outputLocation, templateAttributes);
        }
        Rendition rendition;
        if (!isPropertySet(Property.OUTPUT_RENDITION)) {
            log(LogSeverity.SEVERE, "LOG_OUTPUT_RENDITION_MISSING");
            return;
        } else {
            String renditionName = getProperty(Property.OUTPUT_RENDITION);
            rendition = pluginCtx.findRenditionByName(renditionName);
            if (rendition == null) {
                log(LogSeverity.SEVERE, "LOG_OUTPUT_RENDITION_INVALID", renditionName);
                return;
            }
        }
        String outputMediaItemFilename;
        if (!isPropertySet(Property.OUTPUT_MEDIA_ITEM_FILENAME)) {
            log(LogSeverity.SEVERE, "LOG_OUTPUT_MEDIA_ITEM_FILENAME_MISSING");
            return;
        } else {
            outputMediaItemFilename = getProperty(Property.OUTPUT_MEDIA_ITEM_FILENAME);
        }
        boolean outputNewsItem = false;
        if (isPropertySet(Property.OUTPUT_NEWS_ITEM)) {
            outputNewsItem = getPropertyAsBoolean(Property.OUTPUT_NEWS_ITEM);
        }
        String outputNewsItemTemplate = "";
        String outputNewsItemFilename = "";
        String outputNewsItemDigestTemplateBody = "";
        String outputNewsItemDigestFilename = "";
        String outputNewsItemDigestTemplateHeader = "";
        String outputNewsItemDigestTemplateFooter = "";
        boolean outputNewsItemDigest = false;
        if (outputNewsItem) {
            if (isPropertySet(Property.OUTPUT_NEWS_ITEM_DIGEST)) {
                outputNewsItemDigest = getPropertyAsBoolean(Property.OUTPUT_NEWS_ITEM_DIGEST);
            }
            if (outputNewsItemDigest) {
                if (!isPropertySet(Property.OUTPUT_NEWS_ITEM_DIGEST_TEMPLATE_BODY)) {
                    log(LogSeverity.SEVERE, "LOG_OUTPUT_NEWS_ITEM_DIGEST_TEMPLATE_BODY_MISSING");
                    outputNewsItem = false;
                } else {
                    outputNewsItemDigestTemplateBody = getProperty(Property.OUTPUT_NEWS_ITEM_DIGEST_TEMPLATE_BODY);
                }
                if (!isPropertySet(Property.OUTPUT_NEWS_ITEM_DIGEST_TEMPLATE_HEADER)) {
                    log(LogSeverity.SEVERE, "LOG_OUTPUT_NEWS_ITEM_DIGEST_TEMPLATE_HEADER_MISSING");
                    outputNewsItem = false;
                } else {
                    outputNewsItemDigestTemplateHeader = getProperty(Property.OUTPUT_NEWS_ITEM_DIGEST_TEMPLATE_HEADER);
                }
                if (!isPropertySet(Property.OUTPUT_NEWS_ITEM_DIGEST_TEMPLATE_FOOTER)) {
                    log(LogSeverity.SEVERE, "LOG_OUTPUT_NEWS_ITEM_DIGEST_TEMPLATE_BODY_MISSING");
                    outputNewsItem = false;
                } else {
                    outputNewsItemDigestTemplateFooter = getProperty(Property.OUTPUT_NEWS_ITEM_DIGEST_TEMPLATE_FOOTER);
                }
                if (!isPropertySet(Property.OUTPUT_NEWS_ITEM_DIGEST_FILENAME)) {
                    log(LogSeverity.SEVERE, "LOG_OUTPUT_NEWS_ITEM_DIGEST_FILENAME_MISSING");
                    outputNewsItem = false;
                } else {
                    outputNewsItemDigestFilename = getProperty(Property.OUTPUT_NEWS_ITEM_DIGEST_FILENAME);
                }
            } else {
                if (!isPropertySet(Property.OUTPUT_NEWS_ITEM_TEMPLATE)) {
                    log(LogSeverity.SEVERE, "LOG_OUTPUT_NEWS_ITEM_TEMPLATE_MISSING");
                    outputNewsItem = false;
                } else {
                    outputNewsItemTemplate = getProperty(Property.OUTPUT_NEWS_ITEM_TEMPLATE);
                }
                if (!isPropertySet(Property.OUTPUT_NEWS_ITEM_FILENAME)) {
                    log(LogSeverity.SEVERE, "LOG_OUTPUT_NEWS_ITEM_FILENAME_MISSING");
                    outputNewsItem = false;
                } else {
                    outputNewsItemFilename = getProperty(Property.OUTPUT_NEWS_ITEM_FILENAME);
                }
            }
        }
        if (outputNewsItem) {
            if (outputNewsItemDigest) {
                StringBuilder digest = new StringBuilder();
                Map<String, Object> templateEdition = new HashMap<String, Object>();
                templateEdition.put(TEMPLATE_TAG_EDITION, edition);
                digest.append(compileTemplate(outputNewsItemDigestTemplateHeader, templateEdition));
                for (NewsItemPlacement p : edition.getPlacements()) {
                    Map<String, Object> templateAttributes = new HashMap<String, Object>();
                    templateAttributes.put(TEMPLATE_TAG_NEWS_ITEM_PLACEMENT, p);
                    String outputText = compileTemplate(outputNewsItemDigestTemplateBody, templateAttributes);
                    digest.append(outputText);
                }
                digest.append(compileTemplate(outputNewsItemDigestTemplateFooter, templateEdition));
                String newsItemFilename = compileTemplate(outputNewsItemDigestFilename, templateEdition);
                File newsItemOutput = new File(outputLocation, newsItemFilename);
                try {
                    FileUtils.writeByteArrayToFile(newsItemOutput, digest.toString().getBytes());
                } catch (IOException ex) {
                    log(LogSeverity.SEVERE, "LOG_COULD_NOT_CREATE_FILE_X_BECAUSE_Y", new Object[] { newsItemOutput.getAbsoluteFile().toString(), ex.getMessage() });
                }
            } else {
                for (NewsItemPlacement p : edition.getPlacements()) {
                    Map<String, Object> templateAttributes = new HashMap<String, Object>();
                    templateAttributes.put(TEMPLATE_TAG_NEWS_ITEM_PLACEMENT, p);
                    String outputText = compileTemplate(outputNewsItemTemplate, templateAttributes);
                    String newsItemFilename = compileTemplate(outputNewsItemFilename, templateAttributes);
                    File newsItemOutput = new File(outputLocation, newsItemFilename);
                    try {
                        FileUtils.writeStringToFile(newsItemOutput, outputText);
                    } catch (IOException ex) {
                        log(LogSeverity.SEVERE, "LOG_COULD_NOT_CREATE_FILE_X_BECAUSE_Y", new Object[] { newsItemOutput.getAbsoluteFile().toString(), ex.getMessage() });
                    }
                }
            }
        }
        for (NewsItemPlacement p : edition.getPlacements()) {
            NewsItem ni = p.getNewsItem();
            for (NewsItemMediaAttachment attachment : ni.getMediaAttachments()) {
                MediaItem mediaItem = attachment.getMediaItem();
                try {
                    MediaItemRendition mir = mediaItem.findRendition(rendition);
                    Map<String, Object> templateRendition = new HashMap<String, Object>();
                    templateRendition.put(TEMPLATE_TAG_MEDIA_ITEM_RENDITION, mir);
                    templateRendition.put(TEMPLATE_TAG_EDITION, edition);
                    templateRendition.put(TEMPLATE_TAG_NEWS_ITEM_PLACEMENT, p);
                    String outputFilename = compileTemplate(outputMediaItemFilename, templateRendition);
                    File copyFrom = new File(mir.getFileLocation());
                    File copyTo = new File(outputLocation, outputFilename);
                    try {
                        FileUtils.copyFile(copyFrom, copyTo);
                    } catch (IOException ex) {
                        log(LogSeverity.SEVERE, "LOG_COULD_NOT_COPY_X_TO_Y", new Object[] { copyFrom.getAbsoluteFile().toString(), copyTo.getAbsoluteFile().toString(), ex.getMessage() });
                    }
                } catch (RenditionNotFoundException ex) {
                    log(LogSeverity.WARNING, "LOG_RENDITION_X_NOT_AVAILABLE_FOR_MEDIA_ITEM_Y", new Object[] { rendition.getLabel(), mediaItem.getId() });
                }
            }
        }
    }

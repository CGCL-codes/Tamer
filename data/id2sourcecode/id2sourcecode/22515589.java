    private void outputResult() throws DITAOTException {
        final Properties prop = new Properties();
        final PropertiesWriter writer = new PropertiesWriter();
        final Content content = new ContentImpl();
        final File outputFile = new File(tempDir, FILE_NAME_DITA_LIST);
        final File xmlDitalist = new File(tempDir, FILE_NAME_DITA_LIST_XML);
        final File dir = new File(tempDir);
        final Set<String> copytoSet = new HashSet<String>(INT_128);
        final Set<String> keysDefSet = new HashSet<String>(INT_128);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        prop.put("user.input.dir", baseInputDir);
        prop.put("user.input.file", prefix + inputFile);
        prop.put("user.input.file.listfile", "usr.input.file.list");
        final File inputfile = new File(tempDir, "usr.input.file.list");
        Writer bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(inputfile)));
            bufferedWriter.write(prefix + inputFile);
            bufferedWriter.flush();
        } catch (final FileNotFoundException e) {
            logger.logException(e);
        } catch (final IOException e) {
            logger.logException(e);
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (final IOException e) {
                    logger.logException(e);
                }
            }
        }
        relativeValue = prefix;
        formatRelativeValue = formatRelativeValue(relativeValue);
        prop.put("tempdirToinputmapdir.relative.value", formatRelativeValue);
        prop.put("uplevels", getUpdateLevels());
        addSetToProperties(prop, OUT_DITA_FILES_LIST, outDitaFilesSet);
        addSetToProperties(prop, FULL_DITAMAP_TOPIC_LIST, ditaSet);
        addSetToProperties(prop, FULL_DITA_TOPIC_LIST, fullTopicSet);
        addSetToProperties(prop, FULL_DITAMAP_LIST, fullMapSet);
        addSetToProperties(prop, HREF_DITA_TOPIC_LIST, hrefTopicSet);
        addSetToProperties(prop, CONREF_LIST, conrefSet);
        addSetToProperties(prop, IMAGE_LIST, imageSet);
        addSetToProperties(prop, FLAG_IMAGE_LIST, flagImageSet);
        addSetToProperties(prop, HTML_LIST, htmlSet);
        addSetToProperties(prop, HREF_TARGET_LIST, hrefTargetSet);
        addSetToProperties(prop, HREF_TOPIC_LIST, hrefWithIDSet);
        addSetToProperties(prop, CHUNK_TOPIC_LIST, chunkTopicSet);
        addSetToProperties(prop, SUBJEC_SCHEME_LIST, schemeSet);
        addSetToProperties(prop, CONREF_TARGET_LIST, conrefTargetSet);
        addSetToProperties(prop, COPYTO_SOURCE_LIST, copytoSourceSet);
        addSetToProperties(prop, SUBSIDIARY_TARGET_LIST, subsidiarySet);
        addSetToProperties(prop, CONREF_PUSH_LIST, conrefpushSet);
        addSetToProperties(prop, KEYREF_LIST, keyrefSet);
        addSetToProperties(prop, CODEREF_LIST, coderefSet);
        addSetToProperties(prop, RESOURCE_ONLY_LIST, resourceOnlySet);
        addFlagImagesSetToProperties(prop, REL_FLAGIMAGE_LIST, relFlagImagesSet);
        for (final Map.Entry<String, String> entry : copytoMap.entrySet()) {
            copytoSet.add(entry.toString());
        }
        for (final Map.Entry<String, String> entry : keysDefMap.entrySet()) {
            keysDefSet.add(entry.toString());
        }
        addSetToProperties(prop, COPYTO_TARGET_TO_SOURCE_MAP_LIST, copytoSet);
        addSetToProperties(prop, KEY_LIST, keysDefSet);
        content.setValue(prop);
        writer.setContent(content);
        writer.write(outputFile.getAbsolutePath());
        writer.writeToXML(xmlDitalist.getAbsolutePath());
        writeMapToXML(reader.getRelationshipGrap(), FILE_NAME_SUBJECT_RELATION);
        writeMapToXML(this.schemeDictionary, FILE_NAME_SUBJECT_DICTIONARY);
        if (INDEX_TYPE_ECLIPSEHELP.equals(transtype)) {
            final File pluginIdFile = new File(tempDir, FILE_NAME_PLUGIN_XML);
            DelayConrefUtils.getInstance().writeMapToXML(reader.getPluginMap(), pluginIdFile);
            final StringBuffer result = reader.getResult();
            try {
                export.write(result.toString());
            } catch (final IOException e) {
                logger.logException(e);
            }
        }
    }

    private EmittedArtifact emitManifest(TreeLogger logger, LinkerContext context, EmittedArtifact userManifest, SortedSet<EmittedArtifact> artifacts) throws UnableToCompleteException {
        logger = logger.branch(TreeLogger.DEBUG, "Creating manifest artifact", null);
        StringBuffer out = readManifestTemplate(logger, userManifest);
        digester.update(Util.getBytes(out.toString()));
        Set<Pattern> filters = extractFilters(logger, out);
        for (String pattern : BUILTIN_FILTERS) {
            filters.add(Pattern.compile(pattern));
        }
        String entries = generateEntries(logger, context, filters, artifacts);
        replaceAll(out, "__VERSION__", StringUtils.toHexString(digester.digest()));
        replaceAll(out, "__ENTRIES__", entries.toString());
        return emitBytes(logger, Util.getBytes(out.toString()), context.getModuleName() + ".nocache.manifest");
    }

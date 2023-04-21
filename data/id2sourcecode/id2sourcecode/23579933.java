    public static CssStylesheet exec(TreeLogger logger, URL... stylesheets) throws UnableToCompleteException {
        long mtime = 0;
        for (URL url : stylesheets) {
            long lastModified;
            try {
                lastModified = url.openConnection().getLastModified();
            } catch (IOException e) {
                logger.log(TreeLogger.DEBUG, "Could not determine cached time", e);
                lastModified = 0;
            }
            if (lastModified == 0) {
                mtime = Long.MAX_VALUE;
                break;
            } else {
                mtime = Math.max(mtime, lastModified);
            }
        }
        List<URL> sheets = Arrays.asList(stylesheets);
        SoftReference<CachedStylesheet> ref = SHEETS.get(sheets);
        CachedStylesheet toReturn = ref == null ? null : ref.get();
        if (toReturn != null) {
            if (mtime <= toReturn.getTimestamp()) {
                logger.log(TreeLogger.DEBUG, "Using cached result");
                return toReturn.getCopyOfStylesheet();
            } else {
                logger.log(TreeLogger.DEBUG, "Invalidating cached stylesheet");
            }
        }
        Parser p = new Parser();
        Errors errors = new Errors(logger);
        GenerationHandler g = new GenerationHandler(errors);
        p.setDocumentHandler(g);
        p.setErrorHandler(errors);
        for (URL stylesheet : sheets) {
            TreeLogger branchLogger = logger.branch(TreeLogger.DEBUG, "Parsing CSS stylesheet " + stylesheet.toExternalForm());
            try {
                p.parseStyleSheet(stylesheet.toURI().toString());
                continue;
            } catch (CSSException e) {
                branchLogger.log(TreeLogger.ERROR, "Unable to parse CSS", e);
            } catch (IOException e) {
                branchLogger.log(TreeLogger.ERROR, "Unable to parse CSS", e);
            } catch (URISyntaxException e) {
                branchLogger.log(TreeLogger.ERROR, "Unable to parse CSS", e);
            }
            throw new UnableToCompleteException();
        }
        if (errors.fatalErrorEncountered) {
            throw new UnableToCompleteException();
        }
        toReturn = new CachedStylesheet(g.css, mtime == Long.MAX_VALUE ? 0 : mtime);
        SHEETS.put(new ArrayList<URL>(sheets), new SoftReference<CachedStylesheet>(toReturn));
        return toReturn.getCopyOfStylesheet();
    }

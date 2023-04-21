    public TopicMapSource parseSourceAddress(String source, Locator base, LocatorFactory locFactory) throws ParseException {
        InputStream tmStream = null;
        Locator tmBase = base;
        File sourceFile = new File(source);
        if (sourceFile.exists()) {
            try {
                if (useOnDemandInputStreams) {
                    tmStream = new OnDemandFileInputStream(sourceFile);
                } else {
                    tmStream = new FileInputStream(sourceFile);
                }
                if (tmBase == null) {
                    tmBase = locFactory.createLocator("URI", sourceFile.toURL().toString());
                }
            } catch (FileNotFoundException ex) {
            } catch (MalformedURLException ex) {
                throw new ParseException("Error in converting file name '" + sourceFile.getPath() + "' to a URL: " + ex.getMessage());
            } catch (LocatorFactoryException ex) {
                throw new ParseException("Error in convering URL for file name '" + sourceFile.getPath() + "' into a TM4J Locator. " + ex.getMessage());
            }
        }
        if (tmStream == null) {
            try {
                URL url = new URL(source);
                if (useOnDemandInputStreams) {
                    tmStream = new OnDemandURLInputStream(url);
                } else {
                    tmStream = url.openStream();
                }
                if (tmBase == null) {
                    tmBase = locFactory.createLocator("URI", url.toString());
                }
            } catch (MalformedURLException ex) {
                throw new ParseException("Could not convert '" + source + "' to a local file name or to a URL.", ex);
            } catch (IOException ex) {
                throw new ParseException("Error opening URL '" + source + "'. " + ex.getMessage());
            } catch (LocatorFactoryException ex) {
                throw new ParseException("Error convering URL '" + source + "' to a TM4J Locator." + ex.getMessage());
            }
        }
        if (tmStream == null) {
            throw new ParseException("Unable to parse string '" + source + "' as either a file name or a URL");
        }
        if (tmBase == null) {
            throw new ParseException("Unable to extract a base URI for source '" + source + "'");
        }
        return new SerializedTopicMapSource(tmStream, tmBase);
    }

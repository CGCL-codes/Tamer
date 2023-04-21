    protected Iterable<ScanLocationInformation> obtainLocationsToScan() throws IOException {
        Set<ScanLocationInformation> locations = new HashSet<ScanLocationInformation>();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        for (Iterator<URL> iter = getManifestsToScan(); iter.hasNext(); ) {
            URL url = iter.next();
            log.trace("Checking manifest at URL: " + url);
            InputStream is = url.openStream();
            Manifest mf = new Manifest();
            try {
                mf.read(is);
            } finally {
                is.close();
            }
            if (!parseClassesInPath(url, mf)) continue;
            ScanLocationInformation location = new ScanLocationInformation();
            location.location = url;
            String defaultLocation = getManifestValue(mf, MANIFEST_SECTION_NAME, "RegisterPriority", "NONE");
            try {
                location.defaultPriority = RegisterPriority.valueOf(defaultLocation.toUpperCase());
            } catch (IllegalArgumentException ex) {
                log.error("Illeaal default priority \"%s\". NONE will be used instead", ex);
                location.defaultPriority = RegisterPriority.NONE;
            }
            locations.add(location);
        }
        return locations;
    }

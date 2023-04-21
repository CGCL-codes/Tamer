    private Contribution getBundleModel(Bundle bundle) {
        if (bundle.getBundleId() == 0) return null;
        if (bundle.getSymbolicName() == null) return null;
        if (!isSingleton(bundle)) return null;
        boolean isFragment = InternalPlatform.getDefault().isFragment(bundle);
        if (isFragment) {
            Bundle[] hosts = InternalPlatform.getDefault().getHosts(bundle);
            if (hosts != null && isSingleton(hosts[0]) == false) return null;
        }
        InputStream is = null;
        String manifestType = null;
        String manifestName = isFragment ? FRAGMENT_MANIFEST : PLUGIN_MANIFEST;
        try {
            URL url = bundle.getEntry(manifestName);
            if (url != null) {
                is = url.openStream();
                manifestType = isFragment ? ExtensionsParser.FRAGMENT : ExtensionsParser.PLUGIN;
            }
        } catch (IOException ex) {
            is = null;
        }
        if (is == null) return null;
        try {
            String message = NLS.bind(Messages.parse_problems, bundle.getLocation());
            MultiStatus problems = new MultiStatus(Platform.PI_RUNTIME, ExtensionsParser.PARSE_PROBLEM, message, null);
            ResourceBundle b = null;
            try {
                b = ResourceTranslator.getResourceBundle(bundle);
            } catch (MissingResourceException e) {
            }
            ExtensionsParser parser = new ExtensionsParser(problems);
            Contribution bundleModel = new Contribution(bundle);
            parser.parseManifest(xmlTracker, new InputSource(is), manifestType, manifestName, registry.getObjectManager(), bundleModel, b);
            if (problems.getSeverity() != IStatus.OK) InternalPlatform.getDefault().log(problems);
            return bundleModel;
        } catch (ParserConfigurationException e) {
            logParsingError(bundle, e);
            return null;
        } catch (SAXException e) {
            logParsingError(bundle, e);
            return null;
        } catch (IOException e) {
            logParsingError(bundle, e);
            return null;
        } finally {
            try {
                is.close();
            } catch (IOException ioe) {
            }
        }
    }

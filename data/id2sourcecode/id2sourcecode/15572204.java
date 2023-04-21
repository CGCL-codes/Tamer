    private void installDesignerHome(final String xawareHome, final boolean overwrite) {
        try {
            final String symbolicName = "org.xaware.designer";
            final Bundle bundle = Platform.getBundle(symbolicName);
            final URL url = bundle.getEntry("/home/designerhome.jar");
            final BuildXAwareHomeDesigner xaHomeBuilder = new BuildXAwareHomeDesigner();
            xaHomeBuilder.buildHome(xawareHome, url.openStream(), overwrite);
            xaHomeBuilder.extractJarsFromResources(xawareHome);
        } catch (final Exception e) {
            System.out.println("FAILED TO INSTALL DESIGNER HOME: " + e);
        }
    }

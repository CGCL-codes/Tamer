    private void installDesignerHome(String xawareHome, boolean overwrite) {
        try {
            String symbolicName = "org.xaware.designer";
            Bundle bundle = Platform.getBundle(symbolicName);
            URL url = bundle.getEntry("/home/designerhome.jar");
            BuildXAwareHomeDesigner xaHomeBuilder = new BuildXAwareHomeDesigner();
            xaHomeBuilder.buildHome(xawareHome, url.openStream(), overwrite);
            xaHomeBuilder.extractJarsFromResources(xawareHome);
        } catch (Exception e) {
            System.out.println("FAILED TO INSTALL DESIGNER HOME: " + e);
        }
    }

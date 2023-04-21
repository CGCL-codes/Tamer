    public static boolean isSipServletApplication(DeploymentInfo di) {
        URL url = di.localCl.findResource(SipContext.APPLICATION_SIP_XML);
        if (url != null) {
            try {
                url.openStream();
                return true;
            } catch (IOException e) {
                return false;
            }
        } else {
            File deploymentPath;
            try {
                deploymentPath = new File(di.url.toURI());
            } catch (URISyntaxException e) {
                deploymentPath = new File(di.url.getPath());
            }
            if (deploymentPath.isDirectory()) return SipApplicationAnnotationUtils.findPackageInfoinDirectory(deploymentPath); else return SipApplicationAnnotationUtils.findPackageInfoInArchive(deploymentPath);
        }
    }

    private void copyCSSFileToOutputDirectory() throws IOException {
        Element linkElement;
        try {
            linkElement = XmlUtils.findElementByName(testDoc, "link");
        } catch (JDOMException noLinkElementFound) {
            return;
        }
        File cssFile = new File(linkElement.getAttributeValue("href"));
        if (cssFile.exists()) {
            FileUtils.copyFileToDirectory(cssFile, outputDirectory);
        } else {
            System.err.println("Warning: CSS file " + cssFile.getCanonicalPath() + " could not be found");
        }
    }

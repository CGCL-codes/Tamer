    private void genetateElTld() throws MojoFailureException {
        getLog().info("Generating el tld ...");
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        InputStream xsl = Thread.currentThread().getContextClassLoader().getResourceAsStream("xsl/ELTld.xsl");
        File original = new File(tldDirectory + "/sweetdev-ria-el.tld");
        File source = new File(outputDirectory + "/tlds/source.tld");
        File result = new File(outputDirectory + "/tlds/sweetdev-ria-el.tld");
        try {
            if (!source.exists()) {
                FileUtils.copyFile(original, source);
            }
        } catch (IOException e) {
            throw new MojoFailureException("Problem when trying to copy file: " + original + " in file: " + source, e);
        }
        try {
            Transformer transformer = transformerFactory.newTransformer(new StreamSource(xsl));
            transformer.setParameter("packageName", packageName);
            transformer.transform(new StreamSource(source), new StreamResult(result));
        } catch (TransformerConfigurationException e) {
            throw new MojoFailureException("Problem when trying to generate el tld.", e);
        } catch (TransformerException e) {
            throw new MojoFailureException("Problem when trying to generate el tld.", e);
        }
        try {
            FileUtils.copyFileToDirectory(result, tldDirectory);
        } catch (IOException e) {
            throw new MojoFailureException("Problem when trying to copy file: " + result + " in directory: " + tldDirectory, e);
        }
        getLog().info("Generated el tld.");
    }

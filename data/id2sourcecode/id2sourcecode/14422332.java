    protected void createAirDescriptor(File outputFolder) throws MojoExecutionException {
        try {
            String fileName = mxmlFile != null ? mxmlFile.getName().replace(".mxml", "") : "main";
            File airDescriptor = new File(sourceDirectory.toString() + File.separator + fileName + "-app.xml");
            if (airDescriptor.exists()) {
                getLog().info("Air descriptor file found at : " + airDescriptor.toString());
                if (!airDescriptor.getParentFile().equals(outputFolder)) {
                    FileUtils.copyFile(airDescriptor, new File((outputFolder + File.separator + airDescriptor.getName())));
                    getLog().info("Air descriptor file successfully copied from " + airDescriptor + " to " + outputFolder);
                }
            } else {
                airDescriptor.createNewFile();
                String binaryName = outputArtifactFile != null ? outputArtifactFile.getName() : (fileName + ".swf");
                FileUtils.writeStringToFile(airDescriptor, AbstractFlexMakeHelper.getAirDescriptorContent(project, binaryName, airVersion, airConfig), "UTF-8");
                getLog().info("Air descriptor file successfully generated.");
            }
        } catch (Exception e) {
            throw new MojoExecutionException("Unable to handle Air descriptor file.", e);
        }
    }

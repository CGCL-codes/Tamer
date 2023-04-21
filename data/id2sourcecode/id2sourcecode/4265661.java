    @Override
    public void execute() throws BuildException {
        if (dir == null) throw new BuildException("Please specify a directory", getLocation());
        if (!dir.exists() || !dir.isDirectory()) throw new BuildException("Specified directory doesn't exist", getLocation());
        File creoleXml = new File(dir, "creole.xml");
        if (!creoleXml.exists()) throw new BuildException("Supplied directory isn't a CREOLE plugin");
        try {
            SAXBuilder builder = new SAXBuilder();
            Document creoleDoc = builder.build(creoleXml);
            List<Element> ivyElts = getIvyElements(creoleDoc);
            if (ivyElts.size() > 0) {
                Ivy ivy = getIvy(settings != null ? settings.toURI().toURL() : getSettingsURL(), dir);
                Filter filter = FilterHelper.getArtifactTypeFilter(new String[] { "jar" });
                ResolveOptions resolveOptions = new ResolveOptions();
                resolveOptions.setArtifactFilter(filter);
                if (!verbose) resolveOptions.setLog(LogOptions.LOG_QUIET);
                RetrieveOptions retrieveOptions = new RetrieveOptions();
                retrieveOptions.setArtifactFilter(filter);
                if (!verbose) retrieveOptions.setLog(LogOptions.LOG_QUIET);
                Copy copyTask;
                for (Element e : ivyElts) {
                    File ivyFile = getIvyFile(e, creoleXml);
                    if (!ivyFile.exists()) throw new BuildException("Referenced ivy file does not exist: " + ivyFile, getLocation());
                    Element parent = e.getParentElement();
                    parent.removeContent(e);
                    ResolveReport report = ivy.resolve(ivyFile.toURI().toURL(), resolveOptions);
                    if (report.getAllProblemMessages().size() > 0) throw new BuildException("Unable to resolve all IVY dependencies", getLocation());
                    @SuppressWarnings("unchecked") Map<ArtifactDownloadReport, Set<String>> toCopy = ivy.getRetrieveEngine().determineArtifactsToCopy(report.getModuleDescriptor().getModuleRevisionId(), ivy.getSettings().substitute(ivy.getSettings().getVariable("ivy.retrieve.pattern")), retrieveOptions);
                    for (Map.Entry<ArtifactDownloadReport, Set<String>> entry : toCopy.entrySet()) {
                        ArtifactDownloadReport dlReport = (ArtifactDownloadReport) entry.getKey();
                        for (String destPath : entry.getValue()) {
                            File destFile = new File(destPath);
                            destFile.getParentFile().mkdirs();
                            copyTask = new Copy();
                            copyTask.setProject(getProject());
                            copyTask.setLocation(getLocation());
                            copyTask.setTaskName(getTaskName());
                            copyTask.setFile(dlReport.getLocalFile());
                            copyTask.setTofile(destFile);
                            copyTask.init();
                            copyTask.perform();
                            Element jarElement = new Element("JAR").setText(PersistenceManager.getRelativePath(dir.toURI().toURL(), destFile.toURI().toURL()));
                            parent.addContent(jarElement);
                        }
                        if (fully && !ivyFile.delete()) ivyFile.deleteOnExit();
                    }
                }
                outputter.output(creoleDoc, new FileWriter(creoleXml));
            }
        } catch (Exception e) {
            throw new BuildException(e);
        }
    }

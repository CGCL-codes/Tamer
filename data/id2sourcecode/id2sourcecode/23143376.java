    private void parse(Project project, Object source, GantParsingContext context) throws BuildException {
        InputStream in;
        String buildFileName = null;
        try {
            if (source instanceof File) {
                File buildFile = (File) source;
                buildFileName = buildFile.toString();
                buildFile = FileUtils.getFileUtils().normalize(buildFile.getAbsolutePath());
                context.setBuildFile(buildFile);
                in = new FileInputStream(buildFile);
            } else if (source instanceof URL) {
                URL url = (URL) source;
                buildFileName = url.toString();
                in = url.openStream();
            } else {
                throw new BuildException("Source " + source.getClass().getName() + " not supported by this plugin");
            }
        } catch (IOException e) {
            throw new BuildException("Error reading groovy file " + buildFileName + ": " + e.getMessage(), e);
        }
        if (project.getProperty("basedir") != null) {
            project.setBasedir(project.getProperty("basedir"));
        } else {
            project.setBasedir(context.getBuildFileParent().getAbsolutePath());
        }
        GantProject gantProject;
        if (project instanceof GantProject) {
            gantProject = (GantProject) project;
        } else {
            gantProject = new GantProject(project, context, buildFileName);
        }
        defineDefaultTasks(gantProject);
        GantBuilder antBuilder = new GantBuilder(gantProject);
        gantProject.addReference(REFID_BUILDER, antBuilder);
        Binding binding = new GantBinding(gantProject, antBuilder);
        GroovyShell groovyShell = new GroovyShell(getClass().getClassLoader(), binding);
        final Script script;
        try {
            script = groovyShell.parse(in, buildFileName);
        } catch (CompilationFailedException e) {
            throw new BuildException("Error reading groovy file " + buildFileName + ": " + e.getMessage(), e);
        }
        script.setBinding(binding);
        script.setMetaClass(new GantScriptMetaClass(script.getMetaClass(), gantProject, antBuilder, context));
        new GroovyRunner() {

            @Override
            protected void doRun() {
                script.run();
            }
        }.run();
    }

    public void execute() throws MojoExecutionException, MojoFailureException {
        if (source.exists()) {
            File parent = target.getParentFile();
            if (parent != null && !parent.exists()) parent.mkdirs();
            try {
                FileUtils.copyFile(source, target);
            } catch (IOException e) {
                throw new MojoExecutionException("Error copying resource file", e);
            }
        }
        Resource resource = new Resource();
        resource.setDirectory(target.getParentFile().getAbsolutePath());
        List<String> includes = new ArrayList<String>();
        includes.add(target.getName());
        resource.setIncludes(includes);
        mavenProject.addResource(resource);
    }

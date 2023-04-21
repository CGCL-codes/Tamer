    private IsolatedClassLoader createClassloaderFromDependencies(Collection dependencies, ClassLoader parent, ArtifactResolver resolver) throws Exception {
        System.out.println("Checking for dependencies ...");
        resolver.downloadDependencies(dependencies);
        IsolatedClassLoader cl;
        if (parent == null) {
            cl = new IsolatedClassLoader();
        } else {
            cl = new IsolatedClassLoader(parent);
        }
        for (Iterator i = dependencies.iterator(); i.hasNext(); ) {
            Dependency dependency = (Dependency) i.next();
            File f = resolver.getArtifactFile(dependency);
            if (!f.exists()) {
                String msg = (!resolver.isOnline() ? "; run again online" : "; there was a problem downloading it earlier");
                throw new FileNotFoundException("Missing dependency: " + dependency + msg);
            }
            File newFile = File.createTempFile("maven-bootstrap", "dep");
            newFile.deleteOnExit();
            FileUtils.copyFile(f, newFile);
            cl.addURL(newFile.toURL());
        }
        return cl;
    }

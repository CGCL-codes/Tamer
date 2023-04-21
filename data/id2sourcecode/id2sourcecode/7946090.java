    public void execute() throws MojoExecutionException {
        m_bundleIds = new ArrayList();
        setupRuntimeHelpers();
        StringBuilder addBundles = new StringBuilder();
        boolean firstBundle = true;
        Iterator i = m_project.getArtifacts().iterator();
        while (i.hasNext()) {
            Artifact art = (Artifact) i.next();
            try {
                if (!art.getScope().equals("test")) {
                    String jarUrl = "jar:file://" + art.getFile() + "!/";
                    URL url = new URL(jarUrl);
                    URLConnection uc = url.openConnection();
                    if (uc instanceof JarURLConnection) {
                        JarURLConnection jarurl = (JarURLConnection) uc;
                        JarFile jarfile = jarurl.getJarFile();
                        Attributes attr = jarfile.getManifest().getMainAttributes();
                        String bsymbol = attr.getValue("Bundle-SymbolicName");
                        if (bsymbol != null) {
                            if (!firstBundle) addBundles.append(','); else firstBundle = false;
                            int cleanupPos = bsymbol.indexOf(';');
                            if (cleanupPos > -1) {
                                bsymbol = bsymbol.substring(0, cleanupPos);
                            }
                            addBundles.append(makeBootstrapString(bsymbol, art.getVersion()));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!firstBundle) addBundles.append(','); else firstBundle = false;
        Artifact art = m_project.getArtifact();
        String symbolic = m_project.getProperties().getProperty("bundle.symbolicName");
        if (symbolic == null) System.out.println("Property \"bundle.symbolicName\" for this project is missing, ignoring this bundle!\n"); else addBundles.append(makeBootstrapString(symbolic, art.getVersion()));
        if (!firstBundle) addBundles.append(','); else firstBundle = false;
        String version;
        version = m_project.getProperties().getProperty("coos.version");
        if (version == null) version = m_project.getVersion();
        version = version.replace('-', '.');
        addBundles.append("(&(symbolicname=org.coosproject.messaging.impl)(version>=" + version + "))");
        deployBundles("file://" + m_localRepo.getBasedir() + "/repository.xml", addBundles.toString());
    }

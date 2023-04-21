    @SuppressWarnings("deprecation")
    protected Class findClassInDependencies(String name, Collection<Map> deps) throws Exception {
        Class result = null;
        for (Map module : deps) {
            try {
                if (module.equals(moduleDep)) {
                    try {
                        result = super.findClass(name);
                    } catch (ClassNotFoundException e) {
                        String path = name.replace('.', '/') + ".groovy";
                        URL url = super.findResource(path);
                        if (url == null) throw e;
                        try {
                            result = parseClass(url.openStream());
                        } finally {
                            url.openConnection().setDefaultUseCaches(false);
                        }
                    }
                    loadedClasses.add(result);
                } else {
                    ModuleClassLoader mcl = moduleManager.getMcl(module);
                    if (mcl == null) {
                        moduleManager.assureModuleStarted(module);
                        mcl = moduleManager.getMcl(module);
                        startedDeps.add(module);
                    }
                    result = mcl.loadClass(name);
                    loadedDeps.add(module);
                }
                trace("SUCCESS: MCL[" + module + "]: Successfuly load class: " + name);
                break;
            } catch (ClassNotFoundException e) {
            }
        }
        return result;
    }

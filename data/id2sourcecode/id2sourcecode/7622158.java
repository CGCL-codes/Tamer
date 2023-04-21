    private void addModules(Bundle bundle) {
        if (!resolvedBundles.containsKey(bundle)) {
            Enumeration enumeration = bundle.findEntries("META-INF", "*module.xml", false);
            List<AxisModule> moduleList = null;
            if (enumeration != null) {
                moduleList = new ArrayList<AxisModule>();
            }
            while (enumeration != null && enumeration.hasMoreElements()) {
                try {
                    URL url = (URL) enumeration.nextElement();
                    AxisModule axismodule = new AxisModule();
                    ClassLoader loader = new BundleClassLoader(bundle, Registry.class.getClassLoader());
                    axismodule.setModuleClassLoader(loader);
                    AxisConfiguration axisConfig = configCtx.getAxisConfiguration();
                    ModuleBuilder builder = new ModuleBuilder(url.openStream(), axismodule, axisConfig);
                    Dictionary headers = bundle.getHeaders();
                    String bundleSymbolicName = (String) headers.get("Bundle-SymbolicName");
                    if (bundleSymbolicName != null && bundleSymbolicName.length() != 0) {
                        axismodule.setName(bundleSymbolicName);
                    }
                    String bundleVersion = (String) headers.get("Bundle-Version");
                    if (bundleVersion != null && bundleVersion.length() != 0) {
                        String moduleVersion = "SNAPSHOT";
                        String[] versionSplit = bundleVersion.split("\\.");
                        if (versionSplit.length == 3) {
                            moduleVersion = versionSplit[0] + "." + versionSplit[1] + versionSplit[2];
                        } else if (versionSplit.length == 2) {
                            moduleVersion = versionSplit[0] + "." + versionSplit[1];
                        } else if (versionSplit.length == 1) {
                            moduleVersion = versionSplit[0];
                        }
                        axismodule.setVersion(moduleVersion);
                    }
                    builder.populateModule();
                    axismodule.setParent(axisConfig);
                    AxisModule module = axisConfig.getModule(axismodule.getName());
                    if (module == null) {
                        DeploymentEngine.addNewModule(axismodule, axisConfig);
                        Module moduleObj = axismodule.getModule();
                        if (moduleObj != null) {
                            moduleObj.init(configCtx, axismodule);
                        }
                        moduleList.add(axismodule);
                        log.info("[Axis2/OSGi] Starting any modules in Bundle - " + bundle.getSymbolicName() + " - Module Name : " + axismodule.getName() + " - Module Version : " + axismodule.getVersion());
                    } else {
                        log.info("[ModuleRegistry] Module : " + axismodule.getName() + " is already available.");
                    }
                    Utils.calculateDefaultModuleVersion(axisConfig.getModules(), axisConfig);
                    serviceRegistry.resolve();
                } catch (IOException e) {
                    String msg = "Error while reading module.xml";
                    log.error(msg, e);
                }
            }
            if (moduleList != null && moduleList.size() > 0) {
                resolvedBundles.put(bundle, moduleList);
            }
        }
    }

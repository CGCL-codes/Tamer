    public void execute() throws MojoExecutionException {
        GlobalConfig.reset();
        GlobalConfigurator.setDocroot(prjdir.getAbsolutePath());
        reflection = Reflection.create(project);
        File builddir = new File(project.getBuild().getOutputDirectory());
        try {
            wsgenDirs = new HashSet<File>();
            File confDir = prjFile.getParentFile();
            String projectName = confDir.getParentFile().getName();
            Document doc = Reflection.loadDoc(prjFile);
            Element serviceElem = (Element) doc.getElementsByTagName("webservice-service").item(0);
            if (serviceElem != null) {
                Configuration srvConf = null;
                Element configFileElem = (Element) serviceElem.getElementsByTagName("config-file").item(0);
                if (configFileElem == null) throw new MojoExecutionException("The 'webservice-service' element requires " + " a 'config-file' child element in file '" + prjFile.getAbsolutePath() + "'.");
                String configFileUri = configFileElem.getTextContent().trim();
                FileResource configFile = ResourceUtil.getFileResource(configFileUri);
                if (configFile.exists()) {
                    srvConf = ConfigurationReader.read(configFile);
                } else {
                    srvConf = new Configuration();
                }
                File springConfigFile = new File(prjFile.getParentFile(), "spring.xml");
                FileResource springConfigRes = ResourceUtil.getFileResource(springConfigFile.toURI());
                if (springConfigFile.exists()) {
                    List<ServiceConfig> serviceList = WebServiceBeanConfigReader.read(springConfigRes);
                    srvConf.addServiceConfigs(serviceList);
                }
                if (srvConf.getServiceConfig().size() > 0) {
                    int wsdlCount = 0;
                    int stubCount = 0;
                    File tmpDir = getTmpDir(projectName);
                    GlobalServiceConfig globConf = srvConf.getGlobalServiceConfig();
                    Configuration refSrvConf = null;
                    GlobalServiceConfig refGlobConf = null;
                    boolean globalConfChanged = false;
                    FileResource refWsConfFile = ResourceUtil.getFileResource("file://" + tmpDir.getAbsolutePath() + "/" + "webservice.conf.ser");
                    if (refWsConfFile.exists()) {
                        try {
                            refSrvConf = ConfigurationReader.deserialize(refWsConfFile);
                            refGlobConf = refSrvConf.getGlobalServiceConfig();
                            if (!globConf.equals(refGlobConf)) globalConfChanged = true;
                        } catch (Exception x) {
                            getLog().debug("Error deserializing old reference configuration");
                            getLog().warn("Warning: Ignore old reference configuration because it can't be deserialized. " + "Services will be built from scratch.");
                        }
                    }
                    if (!webappdir.exists()) webappdir.mkdirs();
                    File wsdlDir = tmpDir;
                    if (globConf.getWSDLSupportEnabled()) {
                        String wsdlRepo = globConf.getWSDLRepository();
                        if (wsdlRepo.startsWith("/")) wsdlRepo.substring(1);
                        wsdlDir = new File(webappdir, wsdlRepo);
                        if (!wsdlDir.exists()) {
                            boolean ok = wsdlDir.mkdir();
                            if (!ok) throw new MojoExecutionException("Can't create WSDL directory " + wsdlDir.getAbsolutePath());
                        }
                    }
                    File stubDir = tmpDir;
                    if (globConf.getStubGenerationEnabled()) {
                        String stubRepo = globConf.getStubRepository();
                        if (stubRepo.startsWith("/")) stubRepo.substring(1);
                        stubDir = new File(webappdir, stubRepo);
                        if (!stubDir.exists()) {
                            boolean ok = stubDir.mkdir();
                            if (!ok) throw new MojoExecutionException("Can't create webservice stub directory " + stubDir.getAbsolutePath());
                        }
                    }
                    File webInfDir = new File(webappdir, "WEB-INF");
                    if (!webInfDir.exists()) webInfDir.mkdirs();
                    for (ServiceConfig conf : srvConf.getServiceConfig()) {
                        if (conf.getProtocolType().equals(Constants.PROTOCOL_TYPE_ANY) || conf.getProtocolType().equals(Constants.PROTOCOL_TYPE_SOAP)) {
                            ServiceConfig refConf = null;
                            if (refSrvConf != null) refConf = refSrvConf.getServiceConfig(conf.getName());
                            File wsdlFile = new File(wsdlDir, conf.getName() + ".wsdl");
                            if (refConf == null || !wsdlFile.exists() || globalConfChanged || !conf.equals(refConf) || reflection.checkInterfaceChange(conf.getInterfaceName(), builddir, wsdlFile)) {
                                if (conf.getInterfaceName() != null) checkInterface(conf.getInterfaceName());
                                Class<?> implClass = reflection.clazz(conf.getImplementationName());
                                WebService anno = implClass.getAnnotation(WebService.class);
                                if (anno == null) {
                                    throw new MojoExecutionException("Missing @WebService annotation at service implementation " + "class '" + conf.getImplementationName() + "' of service '" + conf.getName() + "'.");
                                }
                                File wsgenDir = new File(tmpdir, "wsdl/" + conf.getName() + "/" + conf.getImplementationName());
                                if (!wsgenDirs.contains(wsgenDir)) {
                                    if (!wsgenDir.exists()) wsgenDir.mkdirs();
                                    WsGen wsgen = new WsGen();
                                    Project antProject = new Project();
                                    wsgen.setProject(antProject);
                                    wsgen.setDynamicAttribute("keep", "true");
                                    if (!gendir.exists()) gendir.mkdirs();
                                    wsgen.setDynamicAttribute("sourcedestdir", gendir.getAbsolutePath());
                                    wsgen.setDynamicAttribute("genwsdl", "true");
                                    wsgen.setDynamicAttribute("destdir", builddir.getAbsolutePath());
                                    wsgen.setDynamicAttribute("resourcedestdir", wsgenDir.getAbsolutePath());
                                    wsgen.setDynamicAttribute("classpath", reflection.getClasspath());
                                    wsgen.setDynamicAttribute("sei", conf.getImplementationName());
                                    String serviceName = "{" + Reflection.getTargetNamespace(implClass) + "}" + conf.getName();
                                    wsgen.setDynamicAttribute("servicename", serviceName);
                                    try {
                                        wsgen.execute();
                                    } catch (Exception x) {
                                        x.printStackTrace();
                                        throw x;
                                    }
                                    wsgenDirs.add(wsgenDir);
                                }
                                FileUtils.copyFiles(wsgenDir, wsdlDir, ".*wsdl", ".*xsd");
                                wsdlCount++;
                                if (globConf.getStubGenerationEnabled()) {
                                    File stubFile = new File(stubDir, conf.getName() + ".js");
                                    if (!stubFile.exists() || stubFile.lastModified() < wsdlFile.lastModified()) {
                                        Wsdl2Js task = new Wsdl2Js();
                                        task.setInputFile(wsdlFile);
                                        task.setOutputFile(stubFile);
                                        task.generate();
                                        stubCount++;
                                    }
                                }
                            }
                        }
                    }
                    if (wsdlCount > 0) getLog().info("Generated " + wsdlCount + " WSDL file" + (wsdlCount == 1 ? "" : "s") + ".");
                    if (stubCount > 0) getLog().info("Generated " + stubCount + " Javascript stub file" + (stubCount == 1 ? "" : "s") + ".");
                    ConfigurationReader.serialize(srvConf, refWsConfFile);
                }
            }
        } catch (MojoExecutionException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new MojoExecutionException("failure: " + e.getMessage(), e);
        }
    }

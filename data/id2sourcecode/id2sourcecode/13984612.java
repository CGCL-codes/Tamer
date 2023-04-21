    @Override
    public void execute() throws BuildException {
        try {
            GlobalConfigurator.setDocroot(prjdir.getAbsolutePath());
        } catch (IllegalStateException e) {
        }
        try {
            wsgenDirs = new HashSet<File>();
            Document doc = TaskUtils.loadDoc(prjfile);
            NodeList nl = doc.getElementsByTagName("project");
            for (int i = 0; i < nl.getLength(); i++) {
                Element elem = (Element) nl.item(i);
                String prjName = elem.getAttribute("name");
                FileResource wsConfFile = ResourceUtil.getFileResource("file://" + prjdir.getAbsolutePath() + "/" + prjName + "/" + "conf" + "/" + "webservice.conf.xml");
                if (wsConfFile.exists()) {
                    int wsdlCount = 0;
                    int stubCount = 0;
                    File tmpDir = getTmpDir(prjName);
                    Configuration srvConf = ConfigurationReader.read(wsConfFile);
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
                            log("Error deserializing old reference configuration", x, Project.MSG_VERBOSE);
                            log("Warning: Ignore old reference configuration because it can't be deserialized. " + "Services will be built from scratch.", Project.MSG_WARN);
                        }
                    }
                    File appDir = new File(webappsdir, prjName);
                    if (!appDir.exists()) throw new BuildException("Web application directory of project '" + prjName + "' doesn't exist");
                    File wsdlDir = tmpDir;
                    if (globConf.getWSDLSupportEnabled()) {
                        String wsdlRepo = globConf.getWSDLRepository();
                        if (wsdlRepo.startsWith("/")) wsdlRepo.substring(1);
                        wsdlDir = new File(appDir, wsdlRepo);
                        if (!wsdlDir.exists()) {
                            boolean ok = wsdlDir.mkdir();
                            if (!ok) throw new BuildException("Can't create WSDL directory " + wsdlDir.getAbsolutePath());
                        }
                    }
                    File stubDir = tmpDir;
                    if (globConf.getStubGenerationEnabled()) {
                        String stubRepo = globConf.getStubRepository();
                        if (stubRepo.startsWith("/")) stubRepo.substring(1);
                        stubDir = new File(appDir, stubRepo);
                        if (!stubDir.exists()) {
                            boolean ok = stubDir.mkdir();
                            if (!ok) throw new BuildException("Can't create webservice stub directory " + stubDir.getAbsolutePath());
                        }
                    }
                    File webInfDir = new File(appDir, "WEB-INF");
                    if (!webInfDir.exists()) throw new BuildException("Web application WEB-INF subdirectory of project '" + prjName + "' doesn't exist");
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document endPointsDoc = db.newDocument();
                    Element endPointsElem = endPointsDoc.createElementNS(XMLNS_JAXWS_RUNTIME, "ws:endpoints");
                    endPointsElem.setAttribute("version", "2.0");
                    endPointsDoc.appendChild(endPointsElem);
                    boolean hasSOAPService = false;
                    for (ServiceConfig conf : srvConf.getServiceConfig()) {
                        if (conf.getProtocolType().equals(Constants.PROTOCOL_TYPE_ANY) || conf.getProtocolType().equals(Constants.PROTOCOL_TYPE_SOAP)) {
                            hasSOAPService = true;
                            ServiceConfig refConf = null;
                            if (refSrvConf != null) refConf = refSrvConf.getServiceConfig(conf.getName());
                            File wsdlFile = new File(wsdlDir, conf.getName() + ".wsdl");
                            Element endPointElem = endPointsDoc.createElementNS(XMLNS_JAXWS_RUNTIME, "ws:endpoint");
                            endPointsElem.appendChild(endPointElem);
                            endPointElem.setAttribute("name", conf.getName());
                            endPointElem.setAttribute("implementation", conf.getImplementationName() + "JAXWS");
                            endPointElem.setAttribute("url-pattern", globConf.getRequestPath() + "/" + conf.getName());
                            Element chainsElem = endPointsDoc.createElementNS(XMLNS_JAVAEE, "ee:handler-chains");
                            Element chainElem = endPointsDoc.createElementNS(XMLNS_JAVAEE, "ee:handler-chain");
                            chainsElem.appendChild(chainElem);
                            Element handlerElem = endPointsDoc.createElementNS(XMLNS_JAVAEE, "ee:handler");
                            chainElem.appendChild(handlerElem);
                            Element handlerClassElem = endPointsDoc.createElementNS(XMLNS_JAVAEE, "ee:handler-class");
                            handlerElem.appendChild(handlerClassElem);
                            handlerClassElem.setTextContent("de.schlund.pfixcore.webservice.jaxws.ErrorHandler");
                            if (globConf.getMonitoringEnabled() || globConf.getLoggingEnabled()) {
                                handlerElem = endPointsDoc.createElementNS(XMLNS_JAVAEE, "ee:handler");
                                chainElem.appendChild(handlerElem);
                                handlerClassElem = endPointsDoc.createElementNS(XMLNS_JAVAEE, "ee:handler-class");
                                handlerElem.appendChild(handlerClassElem);
                                handlerClassElem.setTextContent("de.schlund.pfixcore.webservice.jaxws.RecordingHandler");
                            }
                            endPointElem.appendChild(chainsElem);
                            if (refConf == null || !wsdlFile.exists() || globalConfChanged || !conf.equals(refConf) || TaskUtils.checkInterfaceChange(conf.getInterfaceName(), builddir, wsdlFile)) {
                                checkInterface(conf.getInterfaceName());
                                Class<?> implClass = Class.forName(conf.getImplementationName());
                                File wsgenDir = new File(tmpdir, "wsdl/" + conf.getName() + "/" + conf.getImplementationName());
                                if (!wsgenDirs.contains(wsgenDir)) {
                                    if (!wsgenDir.exists()) wsgenDir.mkdirs();
                                    WsGen wsgen = new WsGen();
                                    wsgen.setProject(getProject());
                                    wsgen.setDynamicAttribute("genwsdl", "true");
                                    wsgen.setDynamicAttribute("destdir", "build");
                                    wsgen.setDynamicAttribute("resourcedestdir", wsgenDir.getAbsolutePath());
                                    wsgen.setDynamicAttribute("classpath", classPath.toString());
                                    wsgen.setDynamicAttribute("sei", conf.getImplementationName() + "JAXWS");
                                    String serviceName = "{" + TaskUtils.getTargetNamespace(implClass) + "}" + conf.getName();
                                    wsgen.setDynamicAttribute("servicename", serviceName);
                                    wsgen.execute();
                                    wsgenDirs.add(wsgenDir);
                                }
                                FileUtils.copyFiles(wsgenDir, wsdlDir, ".*wsdl", ".*xsd");
                                Element srvElem = (Element) elem.getElementsByTagName("servername").item(0);
                                if (srvElem == null) throw new BuildException("Missing servername element in configuration of project '" + prjName + "'");
                                String srvName = srvElem.getTextContent().trim();
                                String srvPort = "";
                                if (standalone) srvPort = ":" + (portbase + 80);
                                String wsUrl = "http://" + srvName + srvPort + globConf.getRequestPath() + "/" + conf.getName();
                                FileUtils.searchAndReplace(wsdlFile, "UTF-8", "REPLACE_WITH_ACTUAL_URL", wsUrl);
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
                    if (wsdlCount > 0) log("Generated " + wsdlCount + " WSDL file" + (wsdlCount == 1 ? "" : "s") + ".");
                    if (stubCount > 0) log("Generated " + stubCount + " Javascript stub file" + (stubCount == 1 ? "" : "s") + ".");
                    File endPointsFile = new File(webInfDir, "sun-jaxws.xml");
                    if (hasSOAPService && (!endPointsFile.exists() || wsdlCount > 0)) {
                        Transformer t = TransformerFactory.newInstance().newTransformer();
                        t.setOutputProperty(OutputKeys.INDENT, "yes");
                        t.transform(new DOMSource(endPointsDoc), new StreamResult(new FileOutputStream(endPointsFile)));
                        log("Generated JAXWS runtime endpoint configuration.");
                    }
                    ConfigurationReader.serialize(srvConf, refWsConfFile);
                }
            }
        } catch (Exception x) {
            throw new BuildException(x);
        }
    }

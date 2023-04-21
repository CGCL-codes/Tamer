    private void deploymentPreparation(byte[] deploymentPackage, String deploymentPackageName) throws DeployFault {
        log.info("Deployment: Storing deployment package");
        if (this.deploymentDirectory == null || this.deploymentDirectory.equals("") || !(new File(deploymentDirectory).exists())) {
            log.error("Invalid deployment directory:\"" + deploymentDirectory + "\" Please check the property \"" + BISGridProperties.BISGRID_DEPLOYMENT_DIRECTORY + "\" in the BIS-Grid properties file and/or create the directory if missing.");
            throw new DeployFault("Internal error (invalid deployment directory). Please contact the system administrator.");
        }
        log.info("Deployment: Using \"" + deploymentDirectory + "\" as deployment directory.");
        log.debug("Deployment: Creating directory structure in deployment directory.");
        if (!new File(workingDirectoryDeploymentPackage).mkdirs() || !new File(workingDirectoryBPELEngine).mkdirs()) {
            throw new DeployFault("Could not prepare working directory. Please contact the system administrator.");
        }
        log.debug("Deployment: Saving deployment archive to deployment directory.");
        String deplomentPackageFullpath = this.workingDirectoryDeploymentPackage + File.separator + this.filterIllegalCharacters(deploymentPackageName) + ".zip";
        File deploymentPackageFile = new File(deplomentPackageFullpath);
        FileChannel outChannel = null;
        try {
            outChannel = new FileOutputStream(deploymentPackageFile).getChannel();
            outChannel.write(ByteBuffer.wrap(deploymentPackage));
        } catch (FileNotFoundException e) {
            throw new DeployFault("Deployment failed due to a FileNotFoundException: " + e.getMessage());
        } catch (IOException e) {
            throw new DeployFault("Deployment failed due to an IOException: " + e.getMessage());
        } finally {
            try {
                if (outChannel != null) outChannel.close();
            } catch (IOException e) {
            }
        }
        log.debug("Deployment: Open workflow deployment package and filling resource properties.");
        DeploymentPackageDocument deploymentPackagePropertyInstance = DeploymentPackageDocument.Factory.newInstance();
        deploymentPackagePropertyInstance.setDeploymentPackage(deploymentPackage);
        this.properties.put(deploymentPackageProperty, new ImmutableResourceProperty(deploymentPackagePropertyInstance));
        DeploymentPackageNameDocument deploymentPackageNamePropertyInstance = DeploymentPackageNameDocument.Factory.newInstance();
        deploymentPackageNamePropertyInstance.setDeploymentPackageName(deploymentPackageName);
        this.properties.put(deploymentPackageNameProperty, new ImmutableResourceProperty(deploymentPackageNamePropertyInstance));
        InputStream entryInStream = null;
        FileOutputStream entryOutStream = null;
        try {
            ZipFile deploymentPackageZip = new ZipFile(deploymentPackageFile);
            Enumeration<? extends ZipEntry> entries = deploymentPackageZip.entries();
            WsdlFilesDocument wsdlFiles = WsdlFilesDocument.Factory.newInstance();
            wsdlFiles.addNewWsdlFiles();
            XsdFilesDocument xsdFiles = XsdFilesDocument.Factory.newInstance();
            xsdFiles.addNewXsdFiles();
            OtherFilesDocument otherFiles = OtherFilesDocument.Factory.newInstance();
            otherFiles.addNewOtherFiles();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (!entry.isDirectory()) {
                    boolean bpelFileFound = false;
                    if (entry.getName().equals(BISGridConstants.deploymentDescriptorFileName)) {
                        try {
                            de.dgrid.bisgrid.services.management.deployment.ProcessDocument deploymentDescriptor = de.dgrid.bisgrid.services.management.deployment.ProcessDocument.Factory.parse(deploymentPackageZip.getInputStream(entry));
                            de.dgrid.bisgrid.services.management.deployment.ProcessType.References.Other other = deploymentDescriptor.getProcess().getReferences().addNewOther();
                            other.setType("file");
                            other.setLocation("bisgrid/bpel-ws_addressing-transformation.xslt");
                            other.setTypeURI("http://www.w3.org/1999/XSL/Transform");
                            this.properties.put(deploymentDescriptorProperty, new ImmutableResourceProperty(deploymentDescriptor));
                        } catch (XmlException e) {
                            log.error("Could not parse deployment descriptor", e);
                            throw new DeployFault("Could not parse deployment descriptor: " + e.getMessage());
                        }
                    } else if (entry.getName().endsWith(BISGridConstants.bpelFileNameSuffix)) {
                        try {
                            if (!bpelFileFound) {
                                org.oasisOpen.docs.wsbpel.x20.process.executable.ProcessDocument bpelFile = org.oasisOpen.docs.wsbpel.x20.process.executable.ProcessDocument.Factory.parse(deploymentPackageZip.getInputStream(entry));
                                this.properties.put(bpelFileProperty, new ImmutableResourceProperty(bpelFile));
                            } else {
                                throw new DeployFault("Found more than one .bpel file in the deployment archive. Only one file is currently allowed.");
                            }
                        } catch (XmlException e) {
                            log.error("Could not parse WS-BPEL file", e);
                            throw new DeployFault("Could not parse WS-BPEL file: " + e.getMessage());
                        }
                    } else if (entry.getName().endsWith(BISGridConstants.wsdlFileNameSuffix)) {
                        try {
                            DefinitionsDocument wsdlDefinition = DefinitionsDocument.Factory.parse(deploymentPackageZip.getInputStream(entry));
                            WSDLType wsdlEntry = wsdlFiles.getWsdlFiles().addNewWsdl();
                            wsdlEntry.setDefinitions(wsdlDefinition.getDefinitions());
                            wsdlEntry.setName(entry.getName());
                        } catch (XmlException e) {
                            log.error("Could not parse WSDL file", e);
                            throw new DeployFault("Could not parse WSDL file " + entry.getName() + ": " + e.getMessage());
                        }
                    } else if (entry.getName().endsWith(BISGridConstants.xsdFileNameSuffix)) {
                        try {
                            SchemaDocument xsd = SchemaDocument.Factory.parse(deploymentPackageZip.getInputStream(entry));
                            XSDType xsdEntry = xsdFiles.getXsdFiles().addNewXsd();
                            xsdEntry.setSchema(xsd.getSchema());
                            xsdEntry.setName(entry.getName());
                        } catch (XmlException e) {
                            log.error("Could not parse XML schema file", e);
                            throw new DeployFault("Could not parse XML schema file " + entry.getName() + ": " + e.getMessage());
                        }
                    } else {
                        if (!entry.getName().equals("META-INF/MANIFEST.MF")) {
                            byte[] bytes = this.getByteArray(entry, deploymentPackageZip.getInputStream(entry));
                            OtherType otherFile = otherFiles.getOtherFiles().addNewOther();
                            otherFile.setName(entry.getName());
                            otherFile.setBytes(bytes);
                        }
                    }
                }
            }
            XSDType wsAddressingSchema = xsdFiles.getXsdFiles().addNewXsd();
            wsAddressingSchema.setName("bisgrid/ws-addr.xsd");
            wsAddressingSchema.setSchema(SchemaDocument.Factory.parse(ClassLoader.getSystemResourceAsStream("ws-addr.xsd")).getSchema());
            XSDType bisgridMappingSchema = xsdFiles.getXsdFiles().addNewXsd();
            bisgridMappingSchema.setName("bisgrid/bisgrid-mapping-information.xsd");
            bisgridMappingSchema.setSchema(SchemaDocument.Factory.parse(ClassLoader.getSystemResourceAsStream("bisgrid-mapping-information.xsd")).getSchema());
            XSDType bpelServicerefSchema = xsdFiles.getXsdFiles().addNewXsd();
            bpelServicerefSchema.setName("bisgrid/ws-bpel_serviceref.xsd");
            bpelServicerefSchema.setSchema(SchemaDocument.Factory.parse(ClassLoader.getSystemResourceAsStream("ws-bpel_serviceref.xsd")).getSchema());
            OtherType otherFile = otherFiles.getOtherFiles().addNewOther();
            otherFile.setName("bisgrid/bpel-ws_addressing-transformation.xslt");
            otherFile.setBytes(this.getByteArray(ClassLoader.getSystemResourceAsStream("bpel-ws_addressing-transformation.xslt")));
            this.properties.put(wsdlFilesProperty, new ImmutableResourceProperty(wsdlFiles));
            this.properties.put(xsdFilesProperty, new ImmutableResourceProperty(xsdFiles));
            this.properties.put(otherFilesProperty, new ImmutableResourceProperty(otherFiles));
        } catch (ZipException e) {
            log.error("Deployment failed due to a ZipException when processing the deployment archive: ", e);
            throw new DeployFault("Deployment failed due to a ZipException when processing the deployment archive: " + e.getMessage());
        } catch (IOException e) {
            log.error("Deployment failed due to an IOException when processing the deployment archive: ", e);
            throw new DeployFault("Deployment failed due to an IOException when processing the deployment archive: " + e.getMessage());
        } catch (XmlException e) {
            log.error("Deployment failed due to an XmlException when processing the deployment archive: ", e);
            throw new DeployFault("Deployment failed due to an XmlException when processing the deployment archive: " + e.getMessage());
        } finally {
            try {
                if (entryInStream != null) entryInStream.close();
            } catch (Exception e) {
            }
            try {
                if (entryOutStream != null) entryOutStream.close();
            } catch (Exception e) {
            }
        }
    }

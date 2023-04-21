    public HashMap<String, AxisService> processWSDLs(DeploymentFileData file) throws DeploymentException {
        File serviceFile = file.getFile();
        HashMap<String, AxisService> servicesMap = new HashMap<String, AxisService>();
        boolean isDirectory = serviceFile.isDirectory();
        if (isDirectory) {
            try {
                File metaInfFolder = new File(serviceFile, META_INF);
                if (!metaInfFolder.exists()) {
                    metaInfFolder = new File(serviceFile, META_INF.toLowerCase());
                    if (!metaInfFolder.exists()) {
                        throw new DeploymentException(Messages.getMessage(DeploymentErrorMsgs.META_INF_MISSING, serviceFile.getName()));
                    }
                }
                processFilesInFolder(metaInfFolder, servicesMap);
            } catch (FileNotFoundException e) {
                throw new DeploymentException(e);
            } catch (IOException e) {
                throw new DeploymentException(e);
            } catch (XMLStreamException e) {
                throw new DeploymentException(e);
            }
        } else {
            ZipInputStream zin;
            FileInputStream fin;
            try {
                fin = new FileInputStream(serviceFile);
                zin = new ZipInputStream(fin);
                ZipEntry entry;
                byte[] buf = new byte[1024];
                int read;
                ByteArrayOutputStream out;
                while ((entry = zin.getNextEntry()) != null) {
                    String entryName = entry.getName().toLowerCase();
                    if (entryName.startsWith(META_INF.toLowerCase()) && entryName.endsWith(SUFFIX_WSDL)) {
                        out = new ByteArrayOutputStream();
                        if ((entryName.indexOf("/") != entryName.lastIndexOf("/")) || (entryName.indexOf("wsdl_") != -1)) {
                            continue;
                        }
                        while ((read = zin.read(buf)) > 0) {
                            out.write(buf, 0, read);
                        }
                        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
                        OMNamespace documentElementNS = ((OMElement) XMLUtils.toOM(in)).getNamespace();
                        if (documentElementNS != null) {
                            WSDLToAxisServiceBuilder wsdlToAxisServiceBuilder;
                            if (WSDL2Constants.WSDL_NAMESPACE.equals(documentElementNS.getNamespaceURI())) {
                                wsdlToAxisServiceBuilder = new WSDL20ToAllAxisServicesBuilder(new ByteArrayInputStream(out.toByteArray()));
                                wsdlToAxisServiceBuilder.setBaseUri(entryName);
                            } else if (Constants.NS_URI_WSDL11.equals(documentElementNS.getNamespaceURI())) {
                                wsdlToAxisServiceBuilder = new WSDL11ToAllAxisServicesBuilder(new ByteArrayInputStream(out.toByteArray()));
                                ((WSDL11ToAxisServiceBuilder) wsdlToAxisServiceBuilder).setDocumentBaseUri(entryName);
                            } else {
                                throw new DeploymentException(Messages.getMessage("invalidWSDLFound"));
                            }
                            List<AxisService> services = processWSDLFile(wsdlToAxisServiceBuilder, serviceFile, true, new ByteArrayInputStream(out.toByteArray()), entry.getName());
                            if (services != null) {
                                for (int i = 0; i < services.size(); i++) {
                                    AxisService axisService = (AxisService) services.get(i);
                                    if (axisService != null) {
                                        servicesMap.put(axisService.getName(), axisService);
                                    }
                                }
                            }
                        }
                    }
                }
                try {
                    zin.close();
                } catch (IOException e) {
                    log.info(e);
                }
                try {
                    fin.close();
                } catch (IOException e) {
                    log.info(e);
                }
            } catch (FileNotFoundException e) {
                throw new DeploymentException(e);
            } catch (IOException e) {
                throw new DeploymentException(e);
            } catch (XMLStreamException e) {
                throw new DeploymentException(e);
            }
        }
        return servicesMap;
    }

    @Override
    protected boolean executeJob(ETLJob ejJob) {
        try {
            KETLJob kjJob;
            ETLJobStatus jsJobStatus;
            DocumentBuilder builder = null;
            Document xmlDOM = null;
            if (this.ejCurrentJob != null) {
                ResourcePool.logMessage("Error: Cannot executeJob whilst job executing, job should of not been submitted");
                return false;
            }
            this.ejCurrentJob = ejJob;
            if ((ejJob instanceof KETLJob) == false) {
                this.ejCurrentJob = null;
                return false;
            }
            kjJob = (KETLJob) ejJob;
            jsJobStatus = kjJob.getStatus();
            try {
                builder = this.dmfFactory.newDocumentBuilder();
                String jobXML = (String) kjJob.getAction(true);
                if (this.aesOverrideParameters != null) {
                    for (int i = 0; i < this.aesOverrideParameters.size(); i++) {
                        String[] param = (String[]) this.aesOverrideParameters.get(i);
                        if ((param != null) && (param.length == 2)) {
                            jobXML = EngineConstants.replaceParameter(jobXML, param[0], param[1]);
                        }
                    }
                }
                xmlDOM = builder.parse(new InputSource(new StringReader(jobXML)));
                Document xmlParameterList = builder.parse(new InputSource(new StringReader("<ROOT>" + this.msXMLOverride + "</ROOT>")));
                if (this.inheritReferencedXML(xmlDOM, xmlDOM, null, xmlParameterList) == false) {
                    jsJobStatus.setErrorCode(EngineConstants.ERROR_INHERITING_XML_CODE);
                    jsJobStatus.setErrorMessage("Error inheriting Job XML, see log");
                    return false;
                }
                this.ejCurrentJob.setParameterListCache(this.getParameterListsUsed(xmlDOM, new HashMap()));
                if ((this.replaceParameters(xmlDOM, new ArrayList())) == false) {
                    jsJobStatus.setErrorCode(EngineConstants.ERROR_REPLACING_PARAMETER_IN_XML_CODE);
                    jsJobStatus.setErrorMessage("Error replacing parameter lists for Job XML, see log");
                    return false;
                }
                if (this.aesIgnoreQAs != null) {
                    for (String element : this.aesIgnoreQAs) {
                        Node[] aQANodes = XMLHelper.findElementsByName(xmlDOM, QACollection.QA, ETLStep.NAME_ATTRIB, element);
                        if (aQANodes != null) {
                            for (Node element0 : aQANodes) {
                                if (element0.getNodeType() == Node.ELEMENT_NODE) {
                                    Element elementNode = (Element) element0;
                                    elementNode.setAttribute("IGNORE", "TRUE");
                                }
                            }
                        }
                        NodeList qaNodeList = xmlDOM.getElementsByTagName(QACollection.QA);
                        for (int ni = 0; ni < qaNodeList.getLength(); ni++) {
                            NodeList qaNodeChildren = qaNodeList.item(ni).getChildNodes();
                            for (int nix = 0; nix < qaNodeChildren.getLength(); nix++) {
                                Node qaTypeNode = qaNodeChildren.item(nix);
                                if ((qaTypeNode != null) && (qaTypeNode.getNodeType() == Node.ELEMENT_NODE) && XMLHelper.getAttributeAsString(qaTypeNode.getAttributes(), ETLStep.NAME_ATTRIB, "_").equals(element)) {
                                    Element elementNode = (Element) qaTypeNode;
                                    elementNode.setAttribute("IGNORE", "TRUE");
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                jsJobStatus.setErrorCode(EngineConstants.ERROR_READING_JOB_XML_CODE);
                jsJobStatus.setErrorMessage("Error reading job XML: " + e.getMessage());
                ResourcePool.LogException(e, this);
                this.ejCurrentJob = null;
                return false;
            }
            try {
                try {
                    this.em = this.compileJob((Element) xmlDOM.getElementsByTagName("ACTION").item(0));
                    this.ejCurrentJob.setNotificationMode(XMLHelper.getAttributeAsString(xmlDOM.getElementsByTagName("ACTION").item(0).getAttributes(), "EMAILSTATUS", null));
                } catch (java.lang.reflect.InvocationTargetException e) {
                    throw e.getCause();
                }
                this.em.start();
                if (this.mbCommandLine) this.em.monitor(10, 1000); else this.em.monitor(10, 100, jsJobStatus);
            } catch (KETLQAException e) {
                jsJobStatus.setErrorCode(e.getErrorCode());
                jsJobStatus.setErrorMessage("Fatal QA error executing step '" + e.getETLStep().getName() + "'.");
                jsJobStatus.setException(e);
                ResourcePool.LogMessage(this, ResourcePool.ERROR_MESSAGE, this.dumpExceptionCause(e));
                return false;
            } catch (KETLThreadException e) {
                jsJobStatus.setErrorCode(EngineConstants.OTHER_ERROR_EXIT_CODE);
                jsJobStatus.setErrorMessage("Fatal error executing " + (e.getSourceObject() instanceof ETLStep ? "step" : "") + " '" + e.getSourceObject().toString() + "'.");
                jsJobStatus.setException(e);
                ResourcePool.LogMessage(this, ResourcePool.ERROR_MESSAGE, this.dumpExceptionCause(e));
                return false;
            } catch (KETLReadException e) {
                jsJobStatus.setErrorCode(EngineConstants.OTHER_ERROR_EXIT_CODE);
                jsJobStatus.setErrorMessage("Fatal error executing read step '" + e.getSourceThread().getName() + "'.");
                jsJobStatus.setException(e);
                ResourcePool.LogMessage(this, ResourcePool.ERROR_MESSAGE, this.dumpExceptionCause(e));
                return false;
            } catch (KETLTransformException e) {
                jsJobStatus.setErrorCode(EngineConstants.OTHER_ERROR_EXIT_CODE);
                jsJobStatus.setErrorMessage("Fatal error executing transform step '" + e.getSourceThread().getName() + "'.");
                jsJobStatus.setException(e);
                ResourcePool.LogMessage(this, ResourcePool.ERROR_MESSAGE, this.dumpExceptionCause(e));
                return false;
            } catch (KETLWriteException e) {
                jsJobStatus.setErrorCode(EngineConstants.OTHER_ERROR_EXIT_CODE);
                jsJobStatus.setErrorMessage("Fatal error executing write step '" + e.getSourceThread().getName() + "'.");
                jsJobStatus.setException(e);
                ResourcePool.LogMessage(this, ResourcePool.ERROR_MESSAGE, this.dumpExceptionCause(e));
                return false;
            } catch (Throwable e) {
                jsJobStatus.setErrorCode(6);
                jsJobStatus.setErrorMessage("Fatal error executing - '" + e.getMessage() + "'.");
                jsJobStatus.setException(e);
                ResourcePool.LogException(e, this);
                ResourcePool.LogMessage(this, ResourcePool.ERROR_MESSAGE, this.dumpExceptionCause(e));
                return false;
            }
            ResourcePool.LogMessage(this, ResourcePool.INFO_MESSAGE, this.em.finalStatus(jsJobStatus));
            return true;
        } finally {
            try {
                this.closeSteps();
            } finally {
                this.ejCurrentJob = null;
            }
        }
    }

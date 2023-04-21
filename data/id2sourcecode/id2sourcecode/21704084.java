    @Override
    public void executeTask() throws BuildException {
        ProjectClassLoader classLoader = null;
        boolean suppressExceptionOnClose = true;
        try {
            this.log(Messages.getMessage("mergingModules", this.getModel()));
            classLoader = this.newProjectClassLoader();
            final Modules modules = new Modules();
            final Set<ResourceType> resources = new HashSet<ResourceType>(this.getModuleResources());
            final ModelContext context = this.newModelContext(classLoader);
            final Marshaller marshaller = context.createMarshaller(this.getModel());
            final Unmarshaller unmarshaller = context.createUnmarshaller(this.getModel());
            if (this.isModelResourceValidationEnabled()) {
                unmarshaller.setSchema(context.createSchema(this.getModel()));
            }
            if (resources.isEmpty()) {
                final ResourceType defaultResource = new ResourceType();
                defaultResource.setLocation(DefaultModelProvider.getDefaultModuleLocation());
                defaultResource.setOptional(true);
                resources.add(defaultResource);
            }
            for (ResourceType resource : resources) {
                final URL[] urls = this.getResources(context, resource.getLocation());
                if (urls.length == 0) {
                    if (resource.isOptional()) {
                        this.logMessage(Level.WARNING, Messages.getMessage("moduleResourceNotFound", resource.getLocation()));
                    } else {
                        throw new BuildException(Messages.getMessage("moduleResourceNotFound", resource.getLocation()), this.getLocation());
                    }
                }
                for (int i = urls.length - 1; i >= 0; i--) {
                    InputStream in = null;
                    suppressExceptionOnClose = true;
                    try {
                        this.logMessage(Level.FINEST, Messages.getMessage("reading", urls[i].toExternalForm()));
                        final URLConnection con = urls[i].openConnection();
                        con.setConnectTimeout(resource.getConnectTimeout());
                        con.setReadTimeout(resource.getReadTimeout());
                        con.connect();
                        in = con.getInputStream();
                        final Source source = new StreamSource(in, urls[i].toURI().toASCIIString());
                        Object o = unmarshaller.unmarshal(source);
                        if (o instanceof JAXBElement<?>) {
                            o = ((JAXBElement<?>) o).getValue();
                        }
                        if (o instanceof Module) {
                            modules.getModule().add((Module) o);
                        } else if (o instanceof Modules) {
                            modules.getModule().addAll(((Modules) o).getModule());
                        } else {
                            this.log(Messages.getMessage("unsupportedModuleResource", urls[i].toExternalForm()), Project.MSG_WARN);
                        }
                        suppressExceptionOnClose = false;
                    } catch (final SocketTimeoutException e) {
                        String message = Messages.getMessage(e);
                        message = Messages.getMessage("resourceTimeout", message != null ? " " + message : "");
                        if (resource.isOptional()) {
                            this.getProject().log(message, e, Project.MSG_WARN);
                        } else {
                            throw new BuildException(message, e, this.getLocation());
                        }
                    } catch (final IOException e) {
                        String message = Messages.getMessage(e);
                        message = Messages.getMessage("resourceFailure", message != null ? " " + message : "");
                        if (resource.isOptional()) {
                            this.getProject().log(message, e, Project.MSG_WARN);
                        } else {
                            throw new BuildException(message, e, this.getLocation());
                        }
                    } finally {
                        try {
                            if (in != null) {
                                in.close();
                            }
                        } catch (final IOException e) {
                            if (suppressExceptionOnClose) {
                                this.logMessage(Level.SEVERE, Messages.getMessage(e), e);
                            } else {
                                throw new BuildException(Messages.getMessage(e), e, this.getLocation());
                            }
                        }
                    }
                }
                suppressExceptionOnClose = true;
            }
            for (final Iterator<Module> it = modules.getModule().iterator(); it.hasNext(); ) {
                final Module module = it.next();
                if (!this.isModuleIncluded(module) || this.isModuleExcluded(module)) {
                    it.remove();
                    this.log(Messages.getMessage("excludingModule", module.getName()));
                } else {
                    this.log(Messages.getMessage("includingModule", module.getName()));
                }
            }
            Module classpathModule = null;
            if (this.isModelObjectClasspathResolutionEnabled()) {
                classpathModule = modules.getClasspathModule(Modules.getDefaultClasspathModuleName(), classLoader);
                if (classpathModule != null && modules.getModule(Modules.getDefaultClasspathModuleName()) == null) {
                    modules.getModule().add(classpathModule);
                } else {
                    classpathModule = null;
                }
            }
            final ModelValidationReport validationReport = context.validateModel(this.getModel(), new JAXBSource(marshaller, new ObjectFactory().createModules(modules)));
            this.logValidationReport(context, validationReport);
            if (!validationReport.isModelValid()) {
                throw new ModelException(Messages.getMessage("invalidModel", this.getModel()));
            }
            if (classpathModule != null) {
                modules.getModule().remove(classpathModule);
            }
            Module mergedModule = modules.getMergedModule(this.getModuleName());
            mergedModule.setVendor(this.getModuleVendor());
            mergedModule.setVersion(this.getModuleVersion());
            for (int i = 0, s0 = this.getModelObjectStylesheetResources().size(); i < s0; i++) {
                final Transformer transformer = this.getTransformer(this.getModelObjectStylesheetResources().get(i));
                if (transformer != null) {
                    final JAXBSource source = new JAXBSource(marshaller, new ObjectFactory().createModule(mergedModule));
                    final JAXBResult result = new JAXBResult(unmarshaller);
                    transformer.transform(source, result);
                    if (result.getResult() instanceof JAXBElement<?> && ((JAXBElement<?>) result.getResult()).getValue() instanceof Module) {
                        mergedModule = (Module) ((JAXBElement<?>) result.getResult()).getValue();
                    } else {
                        throw new BuildException(Messages.getMessage("illegalTransformationResult", this.getModelObjectStylesheetResources().get(i).getLocation()), this.getLocation());
                    }
                }
            }
            this.log(Messages.getMessage("writingEncoded", this.getModuleFile().getAbsolutePath(), this.getModuleEncoding()));
            marshaller.setProperty(Marshaller.JAXB_ENCODING, this.getModuleEncoding());
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setSchema(context.createSchema(this.getModel()));
            marshaller.marshal(new ObjectFactory().createModule(mergedModule), this.getModuleFile());
            suppressExceptionOnClose = false;
        } catch (final URISyntaxException e) {
            throw new BuildException(Messages.getMessage(e), e, this.getLocation());
        } catch (final JAXBException e) {
            String message = Messages.getMessage(e);
            if (message == null) {
                message = Messages.getMessage(e.getLinkedException());
            }
            throw new BuildException(message, e, this.getLocation());
        } catch (final TransformerConfigurationException e) {
            throw new BuildException(Messages.getMessage(e), e, this.getLocation());
        } catch (final TransformerException e) {
            throw new BuildException(Messages.getMessage(e), e, this.getLocation());
        } catch (final ModelException e) {
            throw new BuildException(Messages.getMessage(e), e, this.getLocation());
        } finally {
            try {
                if (classLoader != null) {
                    classLoader.close();
                }
            } catch (final IOException e) {
                if (suppressExceptionOnClose) {
                    this.logMessage(Level.SEVERE, Messages.getMessage(e), e);
                } else {
                    throw new BuildException(Messages.getMessage(e), e, this.getLocation());
                }
            }
        }
    }

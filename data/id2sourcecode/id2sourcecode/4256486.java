    @Override
    public void executeTask() throws BuildException {
        ProjectClassLoader classLoader = null;
        boolean suppressExceptionOnClose = true;
        try {
            this.log(Messages.getMessage("mergingModlets", this.getModel()));
            classLoader = this.newProjectClassLoader();
            final Modlets modlets = new Modlets();
            final Set<ResourceType> resources = new HashSet<ResourceType>(this.getModletResources());
            final ModelContext context = this.newModelContext(classLoader);
            final Marshaller marshaller = context.createMarshaller(ModletObject.MODEL_PUBLIC_ID);
            final Unmarshaller unmarshaller = context.createUnmarshaller(ModletObject.MODEL_PUBLIC_ID);
            if (this.isModletResourceValidationEnabled()) {
                unmarshaller.setSchema(context.createSchema(ModletObject.MODEL_PUBLIC_ID));
            }
            if (resources.isEmpty()) {
                final ResourceType defaultResource = new ResourceType();
                defaultResource.setLocation(DefaultModletProvider.getDefaultModletLocation());
                defaultResource.setOptional(true);
                resources.add(defaultResource);
            }
            for (ResourceType resource : resources) {
                final URL[] urls = this.getResources(context, resource.getLocation());
                if (urls.length == 0) {
                    if (resource.isOptional()) {
                        this.logMessage(Level.WARNING, Messages.getMessage("modletResourceNotFound", resource.getLocation()));
                    } else {
                        throw new BuildException(Messages.getMessage("modletResourceNotFound", resource.getLocation()));
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
                        if (o instanceof Modlet) {
                            modlets.getModlet().add((Modlet) o);
                        } else if (o instanceof Modlets) {
                            modlets.getModlet().addAll(((Modlets) o).getModlet());
                        } else {
                            this.logMessage(Level.WARNING, Messages.getMessage("unsupportedModletResource", urls[i].toExternalForm()));
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
            for (String defaultExclude : classLoader.getModletExcludes()) {
                final Modlet m = modlets.getModlet(defaultExclude);
                if (m != null) {
                    modlets.getModlet().remove(m);
                }
            }
            modlets.getModlet().addAll(classLoader.getExcludedModlets().getModlet());
            for (final Iterator<Modlet> it = modlets.getModlet().iterator(); it.hasNext(); ) {
                final Modlet modlet = it.next();
                if (!this.isModletIncluded(modlet) || this.isModletExcluded(modlet)) {
                    it.remove();
                    this.log(Messages.getMessage("excludingModlet", modlet.getName()));
                } else {
                    this.log(Messages.getMessage("includingModlet", modlet.getName()));
                }
            }
            final ModelValidationReport validationReport = context.validateModel(ModletObject.MODEL_PUBLIC_ID, new JAXBSource(marshaller, new ObjectFactory().createModlets(modlets)));
            this.logValidationReport(context, validationReport);
            if (!validationReport.isModelValid()) {
                throw new ModelException(Messages.getMessage("invalidModel", ModletObject.MODEL_PUBLIC_ID));
            }
            Modlet mergedModlet = modlets.getMergedModlet(this.getModletName(), this.getModel());
            mergedModlet.setVendor(this.getModletVendor());
            mergedModlet.setVersion(this.getModletVersion());
            for (int i = 0, s0 = this.getModletObjectStylesheetResources().size(); i < s0; i++) {
                final Transformer transformer = this.getTransformer(this.getModletObjectStylesheetResources().get(i));
                if (transformer != null) {
                    final JAXBSource source = new JAXBSource(marshaller, new ObjectFactory().createModlet(mergedModlet));
                    final JAXBResult result = new JAXBResult(unmarshaller);
                    transformer.transform(source, result);
                    if (result.getResult() instanceof JAXBElement<?> && ((JAXBElement<?>) result.getResult()).getValue() instanceof Modlet) {
                        mergedModlet = (Modlet) ((JAXBElement<?>) result.getResult()).getValue();
                    } else {
                        throw new BuildException(Messages.getMessage("illegalTransformationResult", this.getModletObjectStylesheetResources().get(i).getLocation()), this.getLocation());
                    }
                }
            }
            this.log(Messages.getMessage("writingEncoded", this.getModletFile().getAbsolutePath(), this.getModletEncoding()));
            marshaller.setProperty(Marshaller.JAXB_ENCODING, this.getModletEncoding());
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setSchema(context.createSchema(ModletObject.MODEL_PUBLIC_ID));
            marshaller.marshal(new ObjectFactory().createModlet(mergedModlet), this.getModletFile());
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

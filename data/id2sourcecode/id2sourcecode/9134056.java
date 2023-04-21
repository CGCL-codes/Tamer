    @Override
    public void executeTask() throws BuildException {
        BufferedReader reader = null;
        ProjectClassLoader classLoader = null;
        boolean suppressExceptionOnClose = true;
        try {
            classLoader = this.newProjectClassLoader();
            final ModelContext modelContext = this.newModelContext(classLoader);
            final Model model = this.getModel(modelContext);
            final Marshaller marshaller = modelContext.createMarshaller(this.getModel());
            final ModelValidationReport validationReport = modelContext.validateModel(this.getModel(), new JAXBSource(marshaller, new ObjectFactory().createModel(model)));
            this.logValidationReport(modelContext, validationReport);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, this.getModelEncoding());
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            Model displayModel = new Model();
            displayModel.setIdentifier(this.getModel());
            final Specification s = this.getSpecification(model);
            if (s != null) {
                displayModel.getAny().add(s);
            }
            final Instance i = this.getInstance(model);
            if (i != null) {
                displayModel.getAny().add(i);
            }
            final Module m = this.getModule(model);
            if (m != null) {
                displayModel.getAny().add(m);
            }
            if (displayModel.getAny().isEmpty()) {
                displayModel = model;
            }
            if (this.getModelFile() != null) {
                this.log(Messages.getMessage("writingModelObjects", this.getModel(), this.getModelFile().getAbsolutePath()), Project.MSG_INFO);
                marshaller.marshal(new ObjectFactory().createModel(displayModel), this.getModelFile());
            } else {
                this.log(Messages.getMessage("showingModelObjects", this.getModel()), Project.MSG_INFO);
                final StringWriter writer = new StringWriter();
                marshaller.marshal(new ObjectFactory().createModel(displayModel), writer);
                reader = new BufferedReader(new StringReader(writer.toString()));
                String line;
                while ((line = reader.readLine()) != null) {
                    this.log(line, Project.MSG_INFO);
                }
            }
            suppressExceptionOnClose = false;
        } catch (final IOException e) {
            throw new BuildException(Messages.getMessage(e), e, this.getLocation());
        } catch (final JAXBException e) {
            String message = Messages.getMessage(e);
            if (message == null) {
                message = Messages.getMessage(e.getLinkedException());
            }
            throw new BuildException(message, e, this.getLocation());
        } catch (final ModelException e) {
            throw new BuildException(Messages.getMessage(e), e, this.getLocation());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (final IOException e) {
                if (suppressExceptionOnClose) {
                    this.logMessage(Level.SEVERE, Messages.getMessage(e), e);
                } else {
                    throw new BuildException(Messages.getMessage(e), e, this.getLocation());
                }
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
    }

    public Transformer getTransformer(final TransformerResourceType resource) throws TransformerConfigurationException {
        if (resource == null) {
            throw new NullPointerException("resource");
        }
        InputStream in = null;
        boolean suppressExceptionOnClose = true;
        final URL url = this.getResource(resource.getLocation());
        try {
            if (url != null) {
                final ErrorListener errorListener = new ErrorListener() {

                    public void warning(final TransformerException exception) throws TransformerException {
                        if (getProject() != null) {
                            getProject().log(Messages.getMessage(exception), exception, Project.MSG_WARN);
                        }
                    }

                    public void error(final TransformerException exception) throws TransformerException {
                        throw exception;
                    }

                    public void fatalError(final TransformerException exception) throws TransformerException {
                        throw exception;
                    }
                };
                final URLConnection con = url.openConnection();
                con.setConnectTimeout(resource.getConnectTimeout());
                con.setReadTimeout(resource.getReadTimeout());
                con.connect();
                in = con.getInputStream();
                final TransformerFactory f = TransformerFactory.newInstance();
                f.setErrorListener(errorListener);
                final Transformer transformer = f.newTransformer(new StreamSource(in, url.toURI().toASCIIString()));
                transformer.setErrorListener(errorListener);
                for (Map.Entry<Object, Object> e : System.getProperties().entrySet()) {
                    transformer.setParameter(e.getKey().toString(), e.getValue());
                }
                for (final Iterator<Map.Entry<?, ?>> it = this.getProject().getProperties().entrySet().iterator(); it.hasNext(); ) {
                    final Map.Entry<?, ?> e = it.next();
                    transformer.setParameter(e.getKey().toString(), e.getValue());
                }
                for (int i = 0, s0 = this.getTransformationParameterResources().size(); i < s0; i++) {
                    for (Map.Entry<Object, Object> e : this.getProperties(this.getTransformationParameterResources().get(i)).entrySet()) {
                        transformer.setParameter(e.getKey().toString(), e.getValue());
                    }
                }
                for (int i = 0, s0 = this.getTransformationParameters().size(); i < s0; i++) {
                    final KeyValueType p = this.getTransformationParameters().get(i);
                    transformer.setParameter(p.getKey(), p.getObject(this.getLocation()));
                }
                for (int i = 0, s0 = this.getTransformationOutputProperties().size(); i < s0; i++) {
                    final KeyValueType p = this.getTransformationOutputProperties().get(i);
                    transformer.setOutputProperty(p.getKey(), p.getValue());
                }
                for (int i = 0, s0 = resource.getTransformationParameterResources().size(); i < s0; i++) {
                    for (Map.Entry<Object, Object> e : this.getProperties(resource.getTransformationParameterResources().get(i)).entrySet()) {
                        transformer.setParameter(e.getKey().toString(), e.getValue());
                    }
                }
                for (int i = 0, s0 = resource.getTransformationParameters().size(); i < s0; i++) {
                    final KeyValueType p = resource.getTransformationParameters().get(i);
                    transformer.setParameter(p.getKey(), p.getObject(this.getLocation()));
                }
                for (int i = 0, s0 = resource.getTransformationOutputProperties().size(); i < s0; i++) {
                    final KeyValueType p = resource.getTransformationOutputProperties().get(i);
                    transformer.setOutputProperty(p.getKey(), p.getValue());
                }
                suppressExceptionOnClose = false;
                return transformer;
            } else if (resource.isOptional()) {
                this.log(Messages.getMessage("transformerNotFound", resource.getLocation()), Project.MSG_WARN);
            } else {
                throw new BuildException(Messages.getMessage("transformerNotFound", resource.getLocation()), this.getLocation());
            }
        } catch (final URISyntaxException e) {
            throw new BuildException(Messages.getMessage(e), e, this.getLocation());
        } catch (final SocketTimeoutException e) {
            final String message = Messages.getMessage(e);
            if (resource.isOptional()) {
                this.getProject().log(Messages.getMessage("resourceTimeout", message != null ? " " + message : ""), e, Project.MSG_WARN);
            } else {
                throw new BuildException(Messages.getMessage("resourceTimeout", message != null ? " " + message : ""), e, this.getLocation());
            }
        } catch (final IOException e) {
            final String message = Messages.getMessage(e);
            if (resource.isOptional()) {
                this.getProject().log(Messages.getMessage("resourceFailure", message != null ? " " + message : ""), e, Project.MSG_WARN);
            } else {
                throw new BuildException(Messages.getMessage("resourceFailure", message != null ? " " + message : ""), e, this.getLocation());
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
        return null;
    }

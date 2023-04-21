    public List<Transformer> findTransformers(final ModelContext context, final String location) throws ModelException {
        if (context == null) {
            throw new NullPointerException("context");
        }
        if (location == null) {
            throw new NullPointerException("location");
        }
        try {
            final long t0 = System.currentTimeMillis();
            final List<Transformer> transformers = new LinkedList<Transformer>();
            final TransformerFactory transformerFactory = TransformerFactory.newInstance();
            final Enumeration<URL> resources = context.findResources(location);
            final ErrorListener errorListener = new ErrorListener() {

                public void warning(final TransformerException exception) throws TransformerException {
                    if (context.isLoggable(Level.WARNING)) {
                        context.log(Level.WARNING, exception.getMessage(), exception);
                    }
                }

                public void error(final TransformerException exception) throws TransformerException {
                    if (context.isLoggable(Level.SEVERE)) {
                        context.log(Level.SEVERE, exception.getMessage(), exception);
                    }
                    throw exception;
                }

                public void fatalError(final TransformerException exception) throws TransformerException {
                    if (context.isLoggable(Level.SEVERE)) {
                        context.log(Level.SEVERE, exception.getMessage(), exception);
                    }
                    throw exception;
                }
            };
            transformerFactory.setErrorListener(errorListener);
            int count = 0;
            while (resources.hasMoreElements()) {
                count++;
                final URL url = resources.nextElement();
                if (context.isLoggable(Level.FINE)) {
                    context.log(Level.FINE, this.getMessage("processing", new Object[] { url.toExternalForm() }), null);
                }
                final InputStream in = url.openStream();
                final Transformer transformer = transformerFactory.newTransformer(new StreamSource(in));
                in.close();
                transformer.setErrorListener(errorListener);
                transformers.add(transformer);
            }
            if (context.isLoggable(Level.FINE)) {
                context.log(Level.FINE, this.getMessage("contextReport", new Object[] { count, location, Long.valueOf(System.currentTimeMillis() - t0) }), null);
            }
            return transformers.isEmpty() ? null : transformers;
        } catch (final IOException e) {
            throw new ModelException(e.getMessage(), e);
        } catch (final TransformerConfigurationException e) {
            throw new ModelException(e.getMessage(), e);
        }
    }

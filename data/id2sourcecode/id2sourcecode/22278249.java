        public Templates getTemplates(Subject user) throws ServletException {
            if (uri.startsWith("xmldb:exist://")) {
                String docPath = uri.substring("xmldb:exist://".length());
                DocumentImpl doc = null;
                DBBroker broker = null;
                try {
                    broker = pool.get(user);
                    doc = broker.getXMLResource(XmldbURI.create(docPath), Lock.READ_LOCK);
                    if (doc == null) {
                        throw new ServletException("Stylesheet not found: " + docPath);
                    }
                    if (!isCaching() || (doc != null && (templates == null || doc.getMetadata().getLastModified() > lastModified))) {
                        templates = getSource(broker, doc);
                    }
                    lastModified = doc.getMetadata().getLastModified();
                } catch (PermissionDeniedException e) {
                    throw new ServletException("Permission denied to read stylesheet: " + uri, e);
                } catch (EXistException e) {
                    throw new ServletException("Error while reading stylesheet source from db: " + e.getMessage(), e);
                } finally {
                    pool.release(broker);
                    if (doc != null) {
                        doc.getUpdateLock().release(Lock.READ_LOCK);
                    }
                }
            } else {
                try {
                    URL url = new URL(uri);
                    URLConnection connection = url.openConnection();
                    long modified = connection.getLastModified();
                    if (!isCaching() || (templates == null || modified > lastModified || modified == 0)) {
                        LOG.debug("compiling stylesheet " + url.toString());
                        templates = factory.newTemplates(new StreamSource(connection.getInputStream()));
                    }
                    lastModified = modified;
                } catch (IOException e) {
                    throw new ServletException("Error while reading stylesheet source from uri: " + uri + ": " + e.getMessage(), e);
                } catch (TransformerConfigurationException e) {
                    throw new ServletException("Error while reading stylesheet source from uri: " + uri + ": " + e.getMessage(), e);
                }
            }
            return templates;
        }

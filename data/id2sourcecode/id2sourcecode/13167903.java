        public Templates getTemplates() throws TransformerConfigurationException, IOException, XPathException {
            if (uri.startsWith(XmldbURI.EMBEDDED_SERVER_URI_PREFIX)) {
                String docPath = uri.substring(XmldbURI.EMBEDDED_SERVER_URI_PREFIX.length());
                DocumentImpl doc = null;
                try {
                    doc = context.getBroker().getXMLResource(XmldbURI.create(docPath), Lock.READ_LOCK);
                    if (!caching || (doc != null && (templates == null || doc.getMetadata().getLastModified() > lastModified))) templates = getSource(doc);
                    lastModified = doc.getMetadata().getLastModified();
                } catch (PermissionDeniedException e) {
                    throw new XPathException(Transform.this, "Permission denied to read stylesheet: " + uri);
                } finally {
                    if (doc != null) doc.getUpdateLock().release(Lock.READ_LOCK);
                }
            } else {
                URL url = new URL(uri);
                URLConnection connection = url.openConnection();
                long modified = connection.getLastModified();
                if (!caching || (templates == null || modified > lastModified || modified == 0)) {
                    LOG.debug("compiling stylesheet " + url.toString());
                    InputStream is = connection.getInputStream();
                    try {
                        templates = factory.newTemplates(new StreamSource(is));
                    } finally {
                        is.close();
                    }
                }
                lastModified = modified;
            }
            return templates;
        }

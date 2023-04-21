    public static Object loadObjectFromUrl(URL url) throws PersistenceException, IOException, ResourceInstantiationException {
        ProgressListener pListener = (ProgressListener) Gate.getListeners().get("gate.event.ProgressListener");
        StatusListener sListener = (gate.event.StatusListener) Gate.getListeners().get("gate.event.StatusListener");
        if (pListener != null) pListener.progressChanged(0);
        startLoadingFrom(url);
        InputStream rawStream = null;
        try {
            long startTime = System.currentTimeMillis();
            boolean xmlStream = isXmlApplicationFile(url);
            ObjectInputStream ois = null;
            HierarchicalStreamReader reader = null;
            XStream xstream = null;
            if (xmlStream) {
                Reader inputReader = new InputStreamReader(rawStream = url.openStream());
                try {
                    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                    XMLStreamReader xsr = inputFactory.createXMLStreamReader(url.toExternalForm(), inputReader);
                    reader = new StaxReader(new QNameMap(), xsr);
                } catch (XMLStreamException xse) {
                    inputReader.close();
                    throw new PersistenceException("Error creating reader", xse);
                }
                xstream = new XStream(new StaxDriver(new XStream11NameCoder())) {

                    protected boolean useXStream11XmlFriendlyMapper() {
                        return true;
                    }
                };
                xstream.setClassLoader(Gate.getClassLoader());
                ois = xstream.createObjectInputStream(reader);
            } else {
                ois = new GateAwareObjectInputStream(url.openStream());
            }
            Object res = null;
            try {
                Iterator urlIter = ((Collection) getTransientRepresentation(ois.readObject())).iterator();
                while (urlIter.hasNext()) {
                    URL anUrl = (URL) urlIter.next();
                    try {
                        Gate.getCreoleRegister().registerDirectories(anUrl);
                    } catch (GateException ge) {
                        Err.prln("Could not reload creole directory " + anUrl.toExternalForm());
                        ge.printStackTrace(Err.getPrintWriter());
                    }
                }
                res = ois.readObject();
                clearCurrentTransients();
                res = getTransientRepresentation(res);
                long endTime = System.currentTimeMillis();
                if (sListener != null) sListener.statusChanged("Loading completed in " + NumberFormat.getInstance().format((double) (endTime - startTime) / 1000) + " seconds");
                return res;
            } catch (ResourceInstantiationException rie) {
                if (sListener != null) sListener.statusChanged("Failure during instantiation of resources.");
                throw rie;
            } catch (PersistenceException pe) {
                if (sListener != null) sListener.statusChanged("Failure during persistence operations.");
                throw pe;
            } catch (Exception ex) {
                if (sListener != null) sListener.statusChanged("Loading failed!");
                throw new PersistenceException(ex);
            } finally {
                if (ois != null) ois.close();
                if (reader != null) reader.close();
            }
        } finally {
            if (rawStream != null) rawStream.close();
            finishedLoading();
            if (pListener != null) pListener.processFinished();
        }
    }

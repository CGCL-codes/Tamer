    public void run() {
        InputStream inputStream = null;
        int length = -1;
        if (path.contains(permalink)) {
            URL url = null;
            try {
                url = new URL(path);
            } catch (final MalformedURLException e) {
                stopImport("Url isn't a permalink!");
                return;
            }
            try {
                final HttpURLConnection con = (HttpURLConnection) url.openConnection();
                inputStream = con.getInputStream();
                length = con.getContentLength();
            } catch (final IOException e) {
                stopImport("Couldn't open http connection!\n" + e.getMessage());
                return;
            }
        } else {
            try {
                inputStream = new FileInputStream(path);
            } catch (final FileNotFoundException e) {
                stopImport("File doesn't exist!");
                return;
            }
        }
        try {
            final ModelManager modelManager = ModelManager.getInstance();
            modelManager.setChanged();
            modelManager.notifyObservers(new ObserverNotification(NotificationType.startBatchProcess));
            modelManager.clearChanged();
            osm = new Osm2Model(pedestrian, filterCyclic);
            osm.addObserver(this);
            boolean missingData = false;
            if (length >= 0) {
                osm.parseFile(inputStream, length);
            } else {
                missingData = osm.parseFile(inputStream);
            }
            setChanged();
            if (osm.somethingImported()) {
                this.notifyObservers(new ObserverNotification(NotificationType.done, null));
            } else {
                this.notifyObservers(new ObserverNotification(NotificationType.nothing, null));
            }
            if (!osm.wasInterrupted()) {
                modelManager.setChanged();
                modelManager.notifyObservers(new ObserverNotification(NotificationType.endBatchProcess, new Boolean(filterDuplicateEdges), new Boolean(missingData)));
                modelManager.clearChanged();
            }
            clearChanged();
        } catch (final Exception e) {
            Logger.getLogger(this.getClass()).error("Error occured during OSM file import", e);
            this.notifyObservers(new ObserverNotification(NotificationType.failed));
        } finally {
            clearChanged();
        }
    }

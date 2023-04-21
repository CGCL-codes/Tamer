    private void loadScannerProperties() {
        ScannerProperties scannerProperties = ScannerProperties.getInstance();
        this.backUpFiles = scannerProperties.getBoolean(Scanner.KEY_USING_BACK_UP, false);
        this.maxWaitTimeForExifToolsMilliseconds = (long) scannerProperties.getInt(Scanner.KEY_WAIT_FOR_EXIFTOOLS_MILLISECONDS, (int) Scanner.DEFAULT_MAX_WAIT_TIME_FOR_EXIFTOOLS);
        this.readFacesOnly = scannerProperties.getBoolean(Scanner.KEY_FACES_READ_ONLY, false);
        this.removeFaces = scannerProperties.getBoolean(Scanner.KEY_FACES_REMOVE, false);
        this.readGeotagsOnly = scannerProperties.getBoolean(Scanner.KEY_GEOTAGS_READ_ONLY, false);
        this.removeGeotags = scannerProperties.getBoolean(Scanner.KEY_GEOTAGS_REMOVE, false);
        this.writePicasaFullNames = scannerProperties.getBoolean(Scanner.KEY_WRITE_FULL_NAMES, true);
        this.recursively = scannerProperties.getBoolean(Scanner.KEY_RECURSIV, true);
        this.contactsFile = scannerProperties.get(Scanner.KEY_CONTACTS, null);
        if (this.contactsFile == null) {
            this.logger.log(Level.SEVERE, "Found no contacts file in scanner properties ''{0}''.", scannerProperties.getPropertiesFile());
            ResourceFinder resFinder = new ResourceFinder();
            this.contactsFile = resFinder.findContactsXML();
            this.logger.log(Level.INFO, "Found and use default contacts.xml: {0}", this.contactsFile);
        }
        this.directory = scannerProperties.get(Scanner.KEY_FOLDER, null);
        if (this.directory == null) {
            this.logger.log(Level.SEVERE, "Found no pictures directory in scanner properties ''{0}''.", scannerProperties.getPropertiesFile());
        }
        this.logger.log(Level.CONFIG, "Loaded Scanner Properties from file ''{0}'': Picasa contacts file = ''{1}'', picture directory to scan = ''{2}'', scan direcetories recursively = ''{3}'', faces read only = ''{4}'', faces remove = ''{5}'', geotags read only = ''{6}'', geotags remove = ''{7}'', write backup files = ''{8}'', use picasa full names = ''{9}''.", new Object[] { ScannerProperties.getInstance().getPropertiesFile(), this.contactsFile, this.directory, this.recursively, this.readFacesOnly, this.removeFaces, this.readGeotagsOnly, this.removeGeotags, this.backUpFiles, this.writePicasaFullNames });
    }

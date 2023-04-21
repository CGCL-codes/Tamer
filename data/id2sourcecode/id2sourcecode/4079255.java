    public synchronized DataImpl open(URL url) throws BadFormException, IOException, VisADException {
        IOException savedIOE = null;
        VisADException savedVE = null;
        try {
            return readData(new BinaryReader(url.openStream()));
        } catch (IOException ioe) {
            savedIOE = ioe;
        } catch (VisADException ve) {
            savedVE = ve;
        }
        try {
            return readSerial(url.openStream());
        } catch (ClassNotFoundException cnfe) {
            if (savedIOE != null) {
                throw savedIOE;
            } else if (savedVE != null) {
                throw savedVE;
            }
            throw new BadFormException("Could not read URL " + url + ": " + cnfe.getMessage());
        } catch (IOException ioe) {
            if (savedIOE != null) {
                throw savedIOE;
            } else if (savedVE != null) {
                throw savedVE;
            }
            throw ioe;
        }
    }

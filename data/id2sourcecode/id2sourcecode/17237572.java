    private long getSequence() throws org.osid.id.IdException {
        if (this.file == null) {
            logger.logError("no sequence file specified");
            throw new org.osid.id.IdException(org.osid.id.IdException.CONFIGURATION_ERROR);
        }
        java.io.File file;
        java.nio.channels.FileChannel channel;
        java.nio.channels.FileLock lock;
        try {
            file = new java.io.File(this.file);
            channel = new java.io.RandomAccessFile(file, "r").getChannel();
            lock = channel.lock(0, Long.MAX_VALUE, true);
        } catch (Exception e) {
            logger.logError("unable to lock file " + this.file + ": " + e.getMessage());
            throw new org.osid.id.IdException(org.osid.id.IdException.OPERATION_FAILED);
        }
        org.apache.xerces.parsers.DOMParser parser = new org.apache.xerces.parsers.DOMParser();
        try {
            parser.parse(this.file);
        } catch (Exception e) {
            logger.logError("cannot parse sequence file " + this.file + ": " + e.getMessage());
            throw new org.osid.id.IdException(org.osid.id.IdException.OPERATION_FAILED);
        } finally {
            try {
                lock.release();
                channel.close();
            } catch (java.io.IOException ie) {
                logger.logError("cannot release lock on file " + this.file + ": " + ie.getMessage());
                throw new org.osid.id.IdException(org.osid.id.IdException.OPERATION_FAILED);
            }
        }
        org.w3c.dom.Document doc = parser.getDocument();
        org.w3c.dom.NodeList nodes = doc.getElementsByTagName("sequence");
        if (nodes == null) {
            logger.logError("no id node, error in parsing " + this.file);
            throw new org.osid.id.IdException(org.osid.id.IdException.OPERATION_FAILED);
        }
        if (nodes.getLength() != 1) {
            logger.logError("multiple nodes, error in parsing " + this.file);
            throw new org.osid.id.IdException(org.osid.id.IdException.OPERATION_FAILED);
        }
        org.w3c.dom.Element element = (org.w3c.dom.Element) nodes.item(0);
        String value = element.getFirstChild().getNodeValue();
        long ret;
        try {
            ret = Long.parseLong(value.trim());
        } catch (NumberFormatException nfe) {
            logger.logError("unable to parse: " + value);
            throw new org.osid.id.IdException(org.osid.id.IdException.OPERATION_FAILED);
        }
        if (ret < 0) {
            logger.logError("bad number: " + ret);
            throw new org.osid.id.IdException(org.osid.id.IdException.OPERATION_FAILED);
        }
        return (ret);
    }

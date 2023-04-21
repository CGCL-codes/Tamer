    public org.osid.shared.ByteValueIterator read(long version) throws org.osid.filing.FilingException {
        logger.logMethod(version);
        try {
            Object object = this.asset.getContent();
            if (object instanceof String) {
                String s = (String) object;
                if (s.startsWith("http://")) {
                    java.net.URL url = new java.net.URL(s);
                    return new ByteValueIterator(url.openStream());
                } else {
                    logger.logError("unknown asset content");
                    throw new org.osid.filing.FilingException(org.osid.filing.FilingException.OPERATION_FAILED);
                }
            } else if (object instanceof edu.mit.osidimpl.repository.shared.SerializableInputStream) {
                return new ByteValueIterator(((edu.mit.osidimpl.repository.shared.SerializableInputStream) object).getInputStream());
            } else {
                logger.logError("unknown asset content");
                throw new org.osid.filing.FilingException(org.osid.filing.FilingException.OPERATION_FAILED);
            }
        } catch (java.io.IOException ie) {
            logger.logError("cannot get data", ie);
            throw new org.osid.filing.FilingException(org.osid.filing.FilingException.OPERATION_FAILED);
        } catch (org.osid.repository.RepositoryException re) {
            logger.logError("cannot get asset content", re);
            throw this.mgr.getException(re);
        }
    }

    private XDSDocument checkIdenticalHash(XDSDocument xdsDoc, String documentUID) throws XDSException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            DigestOutputStream dos = new DigestOutputStream(NullOutputStream.STREAM, md);
            xdsDoc.getXdsDocWriter().writeTo(dos);
            String newHash = DocumentStore.toHexString(md.digest());
            BaseDocument doc = docStore.getDocument(documentUID, xdsDoc.getMimeType());
            if (log.isDebugEnabled()) {
                log.debug("checkIdenticalHash verification:\nstored  :" + doc.getHash() + "\nreceived:" + newHash);
            }
            if (!newHash.equals(doc.getHash())) {
                throw new XDSException(XDSConstants.XDS_ERR_NON_IDENTICAL_HASH, "Document " + documentUID + " already exists with non identical hash value!", null);
            }
            return new XDSDocument(doc.getDocumentUID(), doc.getMimeType(), getXdsDocWriter(doc), doc.getHash(), null).setStatus(XDSDocument.STORED);
        } catch (XDSException e) {
            throw e;
        } catch (Exception e) {
            log.error("Cant check hash!", e);
            throw new XDSException(XDSConstants.XDS_ERR_REPOSITORY_ERROR, "Document " + documentUID + " already exists! Failed to check HASH value!", e);
        }
    }

    public void setViewMode(int viewMode) {
        data = new Vector();
        if (cert == null) return;
        if (viewMode == 0 || viewMode == 1) {
            Vector versionRow = new Vector();
            versionRow.add(new JLabel("Version", CertViewer.attributeIcon, SwingConstants.LEFT));
            versionRow.add("V" + cert.getVersion());
            data.add(versionRow);
            Vector serialNumberRow = new Vector();
            serialNumberRow.add(new JLabel("Serial Number", CertViewer.attributeIcon, SwingConstants.LEFT));
            serialNumberRow.add(CBParse.bytes2Hex(cert.getSerialNumber().toByteArray()));
            data.add(serialNumberRow);
            Vector sigAlgRow = new Vector();
            sigAlgRow.add(new JLabel("Signature Algorithm", CertViewer.attributeIcon, SwingConstants.LEFT));
            sigAlgRow.add(cert.getSigAlgName());
            data.add(sigAlgRow);
            Vector issuerRow = new Vector();
            issuerRow.add(new JLabel("Issuer", CertViewer.attributeIcon, SwingConstants.LEFT));
            issuerRow.add(cert.getIssuerX500Principal().getName());
            data.add(issuerRow);
            Vector fromRow = new Vector();
            fromRow.add(new JLabel("Valid From", CertViewer.attributeIcon, SwingConstants.LEFT));
            fromRow.add(cert.getNotBefore());
            data.add(fromRow);
            Vector toRow = new Vector();
            toRow.add(new JLabel("Valid To", CertViewer.attributeIcon, SwingConstants.LEFT));
            toRow.add(cert.getNotAfter());
            data.add(toRow);
            Vector subjectRow = new Vector();
            subjectRow.add(new JLabel("Subject", CertViewer.attributeIcon, SwingConstants.LEFT));
            subjectRow.add(cert.getSubjectX500Principal().getName());
            data.add(subjectRow);
            Vector publicKeyRow = new Vector();
            publicKeyRow.add(new JLabel("Public Key", CertViewer.attributeIcon, SwingConstants.LEFT));
            PublicKey pubKey = cert.getPublicKey();
            String publicKeyString = pubKey.getAlgorithm();
            if (pubKey instanceof RSAPublicKey) publicKeyString = publicKeyString + " (" + ((RSAPublicKey) pubKey).getModulus().bitLength() + " Bits)"; else if (pubKey instanceof DSAPublicKey) publicKeyString = publicKeyString + " (" + ((DSAPublicKey) pubKey).getY().bitLength() + " Bits)";
            publicKeyRow.add(publicKeyString);
            data.add(publicKeyRow);
        }
        if (viewMode == 0 || viewMode == 2) {
            Set nonCritSet = cert.getNonCriticalExtensionOIDs();
            if (nonCritSet != null && !nonCritSet.isEmpty()) {
                for (Iterator i = nonCritSet.iterator(); i.hasNext(); ) {
                    String oid = (String) i.next();
                    Vector nonCritRow = new Vector();
                    String extname = getNameFromOID(oid);
                    nonCritRow.add(new JLabel(extname, CertViewer.extensionIcon, SwingConstants.LEFT));
                    addExtDetails(nonCritRow, printext(extname, cert.getExtensionValue(oid)).toString());
                    data.add(nonCritRow);
                }
            }
        }
        if (viewMode == 0 || viewMode == 2 || viewMode == 3) {
            Set critSet = cert.getCriticalExtensionOIDs();
            if (critSet != null && !critSet.isEmpty()) {
                for (Iterator i = critSet.iterator(); i.hasNext(); ) {
                    String oid = (String) i.next();
                    Vector critRow = new Vector();
                    String extname = getNameFromOID(oid);
                    critRow.add(new JLabel(extname, CertViewer.criticalExtensionIcon, SwingConstants.LEFT));
                    addExtDetails(critRow, printext(extname, cert.getExtensionValue(oid)).toString());
                    data.add(critRow);
                }
            }
        }
        if (viewMode == 0 || viewMode == 4) {
            Vector thumbprintAlgorithmRow = new Vector();
            thumbprintAlgorithmRow.add(new JLabel("Thumbprint Algorithm", CertViewer.thumbprintIcon, SwingConstants.LEFT));
            thumbprintAlgorithmRow.add("sha1");
            data.add(thumbprintAlgorithmRow);
            try {
                Vector thumbprintRow = new Vector();
                thumbprintRow.add(new JLabel("Thumbprint", CertViewer.thumbprintIcon, SwingConstants.LEFT));
                MessageDigest md = MessageDigest.getInstance("SHA");
                byte[] hash = md.digest(cert.getEncoded());
                thumbprintRow.add(CBParse.bytes2HexSplit(hash, 4));
                data.add(thumbprintRow);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        fireTableStructureChanged();
    }

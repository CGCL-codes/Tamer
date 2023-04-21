    public static InformationCurrencySeries generateIC(URL certURL, PrivateKey privKey, Properties issuanceProps) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(ICWSConstants.DATE_FORMAT_SPECIFICATION);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        int loglevel = -1;
        try {
            int tmpInt = new Integer(issuanceProps.getProperty("client_loglevel")).intValue();
            if (tmpInt > 0 || tmpInt < 6) loglevel = tmpInt;
        } catch (java.lang.Exception e) {
        }
        File tmpFile = File.createTempFile("issuance", ".url");
        log.debug(" writing url " + certURL.toString() + " to file " + tmpFile.toURL().toString());
        FileOutputStream fos = new FileOutputStream(tmpFile);
        URLConnection certConnection = certURL.openConnection();
        InputStream certIS = certConnection.getInputStream();
        while (true) {
            int certInt = certIS.read();
            if (certInt == -1) break;
            fos.write(certInt);
        }
        FileInputStream fileFIS = new FileInputStream(tmpFile);
        MessageDigest md = MessageDigest.getInstance(digestValueStrings[0]);
        DigestInputStream dis = new DigestInputStream(fileFIS, md);
        fos.close();
        fos = new FileOutputStream("/dev/null");
        Utils.bufferedCopy(dis, fos);
        String digestValue = Base64.encode(dis.getMessageDigest().digest());
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document doc = dbf.newDocumentBuilder().newDocument();
        doc.appendChild(doc.createElementNS(ICWSConstants.RDF_NAMESPACE, "rdf:RDF"));
        doc.getDocumentElement().setAttributeNS(ICWSConstants.XMLNS_NAMESPACE, "xmlns:in", ICWSConstants.IN_NAMESPACE);
        doc.getDocumentElement().setAttributeNS(ICWSConstants.XMLNS_NAMESPACE, "xmlns:dc", ICWSConstants.DC_NAMESPACE);
        doc.getDocumentElement().appendChild(doc.createTextNode("\n"));
        Element descElem = (Element) doc.getDocumentElement().appendChild(doc.createElementNS(ICWSConstants.RDF_NAMESPACE, "rdf:Description"));
        descElem.appendChild(doc.createTextNode("\n"));
        doc.getDocumentElement().appendChild(doc.createTextNode("\n"));
        descElem.setAttributeNS(ICWSConstants.RDF_NAMESPACE, "rdf:about", certURL.toString());
        Element idElem = (Element) descElem.appendChild(doc.createElementNS(ICWSConstants.DC_NAMESPACE, "dc:identifier"));
        descElem.appendChild(doc.createTextNode("\n"));
        idElem.setTextContent(certURL.toString());
        Element dvElem = (Element) descElem.appendChild(doc.createElementNS(ICWSConstants.IN_NAMESPACE, digestValueStrings[1]));
        dvElem.setTextContent(digestValue);
        descElem.appendChild(doc.createTextNode("\n"));
        Element dateElem = (Element) descElem.appendChild(doc.createElementNS(ICWSConstants.DC_NAMESPACE, "dc:date"));
        String nowDate = sdf.format(new Date()).trim();
        dateElem.setTextContent(nowDate);
        descElem.appendChild(doc.createTextNode("\n"));
        tmpFile.delete();
        IssuanceRequest certReq = new IssuanceRequest();
        signUnderlierDocument(doc, privKey);
        certReq.setUnderlyingInformation(doc.getDocumentElement());
        try {
            certReq.setCertificateNumber(Integer.parseInt(issuanceProps.getProperty("seriesCertificateNumber")));
        } catch (Exception e) {
        }
        try {
            certReq.setSeriesTimespan(Long.parseLong(issuanceProps.getProperty("seriesTimespan")));
        } catch (Exception e) {
        }
        certReq.setNotAfter(new Date(System.currentTimeMillis() + 50000));
        certReq.setNotBefore(new Date());
        certReq.sign(privKey);
        boolean verified = certReq.verifySignature();
        if (verified) {
            System.out.println("certReq verified.");
        } else {
            System.out.println("certReq did not verify.");
        }
        if (System.getProperty("icws.ssl.trustStore") != null) {
            issuanceProps.setProperty("trustStore", System.getProperty("icws.ssl.trustStore"));
        }
        if (System.getProperty("icws.ssl.endpoint") != null) {
            issuanceProps.setProperty("certification_endpoint", System.getProperty("icws.ssl.endpoint"));
        }
        if (loglevel > 3) {
            File crFile = File.createTempFile("issuance-request-", ".xml", new File(issuanceProps.getProperty("ics_output_directory")));
            certReq.toString(new FileOutputStream(crFile));
            log.debug("Wrote issuance request to " + crFile.getCanonicalPath() + ".");
        }
        String certResponse = IssuanceUtils.processIssuanceRequest(issuanceProps, certReq.toString());
        if (certResponse == null) {
            log.debug("No information currency series was received.");
        } else if (certResponse != null) {
            InformationCurrencySeries ics = new InformationCurrencySeries(new ByteArrayInputStream(Utils.convertEntities(certResponse).getBytes()));
            return ics;
        }
        return null;
    }

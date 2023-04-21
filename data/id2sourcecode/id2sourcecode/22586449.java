    public static Record createImageRecord(String pid, ImageRecordStructure recordStructure, Repository repository, PID objectId, FedoraObjectAssetType assetType, String displayName, String identifier) throws org.osid.repository.RepositoryException {
        Record record = null;
        try {
            record = new Record(new PID(pid), recordStructure);
            if (assetType.getKeyword().equals("tufts/image/archival")) {
                record.createPart(recordStructure.getThumbnailPartStructure().getId(), Utilities.formatObjectUrl(objectId.getIdString(), THUMBNAIL, repository));
                record.createPart(recordStructure.getURLPartStructure().getId(), Utilities.formatObjectUrl(objectId.getIdString(), FULLVIEW, repository));
                record.createPart(recordStructure.getMediumImagePartStructure().getId(), Utilities.formatObjectUrl(objectId.getIdString(), MEDIUM_RES, repository));
                record.createPart(LargeImagePartStructure.getInstance().getId(), Utilities.formatObjectUrl(objectId.getIdString(), MEDIUM_RES, repository));
                String dcURL = Utilities.formatObjectUrl(objectId.getIdString(), "bdef:TuftsMetadata/getDublinCore/", repository);
                String contributor = "";
                String coverage = "";
                String creator = "";
                String date = "";
                String description = "";
                String format = "";
                String language = "";
                String publisher = "";
                String relation = "";
                String rights = "";
                String source = "";
                String subject = "";
                String type = "";
                try {
                    java.net.URL url = new java.net.URL(dcURL);
                    java.net.URLConnection connection = url.openConnection();
                    java.net.HttpURLConnection http = (java.net.HttpURLConnection) connection;
                    java.io.InputStreamReader in = new java.io.InputStreamReader(http.getInputStream());
                    StringBuffer xml = new StringBuffer();
                    try {
                        int i = 0;
                        while ((i = in.read()) != -1) {
                            xml.append(Character.toString((char) i));
                        }
                    } catch (Throwable t) {
                    }
                    javax.xml.parsers.DocumentBuilderFactory dbf = null;
                    javax.xml.parsers.DocumentBuilder db = null;
                    org.w3c.dom.Document document = null;
                    dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
                    db = dbf.newDocumentBuilder();
                    document = db.parse(new java.io.ByteArrayInputStream(xml.toString().getBytes()));
                    org.w3c.dom.NodeList dcs = document.getElementsByTagName("dc:contributor");
                    int numDCs = dcs.getLength();
                    for (int i = 0; i < numDCs; i++) {
                        org.w3c.dom.Element dc = (org.w3c.dom.Element) dcs.item(i);
                        if (dc.hasChildNodes()) {
                            contributor = dc.getFirstChild().getNodeValue();
                            record.createPart(ContributorPartStructure.getInstance().getId(), contributor);
                        }
                    }
                    dcs = document.getElementsByTagName("dc:creator");
                    numDCs = dcs.getLength();
                    for (int i = 0; i < numDCs; i++) {
                        org.w3c.dom.Element dc = (org.w3c.dom.Element) dcs.item(i);
                        if (dc.hasChildNodes()) {
                            creator = dc.getFirstChild().getNodeValue();
                            record.createPart(CreatorPartStructure.getInstance().getId(), creator);
                        }
                    }
                    dcs = document.getElementsByTagName("dc:date");
                    numDCs = dcs.getLength();
                    for (int i = 0; i < numDCs; i++) {
                        org.w3c.dom.Element dc = (org.w3c.dom.Element) dcs.item(i);
                        if (dc.hasChildNodes()) {
                            date = dc.getFirstChild().getNodeValue();
                            record.createPart(DatePartStructure.getInstance().getId(), date);
                        }
                    }
                    dcs = document.getElementsByTagName("dc:description");
                    numDCs = dcs.getLength();
                    for (int i = 0; i < numDCs; i++) {
                        org.w3c.dom.Element dc = (org.w3c.dom.Element) dcs.item(i);
                        if (dc.hasChildNodes()) {
                            description = dc.getFirstChild().getNodeValue();
                            record.createPart(DescriptionPartStructure.getInstance().getId(), description);
                        }
                    }
                    dcs = document.getElementsByTagName("dc:format");
                    numDCs = dcs.getLength();
                    for (int i = 0; i < numDCs; i++) {
                        org.w3c.dom.Element dc = (org.w3c.dom.Element) dcs.item(i);
                        if (dc.hasChildNodes()) {
                            format = dc.getFirstChild().getNodeValue();
                            record.createPart(FormatPartStructure.getInstance().getId(), format);
                        }
                    }
                    dcs = document.getElementsByTagName("dc:language");
                    numDCs = dcs.getLength();
                    for (int i = 0; i < numDCs; i++) {
                        org.w3c.dom.Element dc = (org.w3c.dom.Element) dcs.item(i);
                        if (dc.hasChildNodes()) {
                            language = dc.getFirstChild().getNodeValue();
                            record.createPart(LanguagePartStructure.getInstance().getId(), language);
                        }
                    }
                    dcs = document.getElementsByTagName("dc:publisher");
                    numDCs = dcs.getLength();
                    for (int i = 0; i < numDCs; i++) {
                        org.w3c.dom.Element dc = (org.w3c.dom.Element) dcs.item(i);
                        if (dc.hasChildNodes()) {
                            publisher = dc.getFirstChild().getNodeValue();
                            record.createPart(PublisherPartStructure.getInstance().getId(), publisher);
                        }
                    }
                    dcs = document.getElementsByTagName("dc:relation");
                    numDCs = dcs.getLength();
                    for (int i = 0; i < numDCs; i++) {
                        org.w3c.dom.Element dc = (org.w3c.dom.Element) dcs.item(i);
                        if (dc.hasChildNodes()) {
                            relation = dc.getFirstChild().getNodeValue();
                            record.createPart(RelationPartStructure.getInstance().getId(), relation);
                        }
                    }
                    dcs = document.getElementsByTagName("dc:rights");
                    numDCs = dcs.getLength();
                    for (int i = 0; i < numDCs; i++) {
                        org.w3c.dom.Element dc = (org.w3c.dom.Element) dcs.item(i);
                        if (dc.hasChildNodes()) {
                            rights = dc.getFirstChild().getNodeValue();
                            record.createPart(RightsPartStructure.getInstance().getId(), rights);
                        }
                    }
                    dcs = document.getElementsByTagName("dc:source");
                    numDCs = dcs.getLength();
                    for (int i = 0; i < numDCs; i++) {
                        org.w3c.dom.Element dc = (org.w3c.dom.Element) dcs.item(i);
                        if (dc.hasChildNodes()) {
                            creator = dc.getFirstChild().getNodeValue();
                            record.createPart(SourcePartStructure.getInstance().getId(), source);
                        }
                    }
                    dcs = document.getElementsByTagName("dc:subject");
                    numDCs = dcs.getLength();
                    for (int i = 0; i < numDCs; i++) {
                        org.w3c.dom.Element dc = (org.w3c.dom.Element) dcs.item(i);
                        if (dc.hasChildNodes()) {
                            creator = dc.getFirstChild().getNodeValue();
                            record.createPart(SubjectPartStructure.getInstance().getId(), subject);
                        }
                    }
                    dcs = document.getElementsByTagName("dc:type");
                    numDCs = dcs.getLength();
                    for (int i = 0; i < numDCs; i++) {
                        org.w3c.dom.Element dc = (org.w3c.dom.Element) dcs.item(i);
                        if (dc.hasChildNodes()) {
                            type = dc.getFirstChild().getNodeValue();
                            record.createPart(TypePartStructure.getInstance().getId(), type);
                        }
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return record;
    }

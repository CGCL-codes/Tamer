    public static void updateDcRecord(String pid, String dcSubject, String dcCreator, String dcPublisher, String dcContributor, String dcDescription) {
        DocumentBuilder builder;
        Document doc;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            String dcRecordURL = PropertyContainer.instance().getProperty(FEDORA_REPOSITORY_URL) + "/get/" + OBJECT_NAMESPACE + pid + "/DC";
            URL url = new URL(dcRecordURL);
            InputSource is = new InputSource(url.openStream());
            doc = builder.parse(is);
            Element root = doc.getDocumentElement();
            Node subjectNode = doc.createElement("dc:subject");
            subjectNode.appendChild(doc.createTextNode(dcSubject));
            root.appendChild(subjectNode);
            Node creatorNode = doc.createElement("dc:creator");
            creatorNode.appendChild(doc.createTextNode(dcCreator));
            root.appendChild(creatorNode);
            Node publisherNode = doc.createElement("dc:publisher");
            publisherNode.appendChild(doc.createTextNode(dcPublisher));
            root.appendChild(publisherNode);
            Node contributorNode = doc.createElement("dc:contributor");
            contributorNode.appendChild(doc.createTextNode(dcContributor));
            root.appendChild(contributorNode);
            Node descriptionNode = doc.createElement("dc:description");
            descriptionNode.appendChild(doc.createTextNode(dcDescription));
            root.appendChild(descriptionNode);
            StringWriter sw = new StringWriter();
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(sw);
            transformer.transform(source, result);
            String xmlString = sw.toString();
            getManagementAPI().modifyDatastreamByValue(pid, "DC", new String[] {}, "Dublin Core Metadata", "text/xml", "", xmlString.getBytes(), null, null, "Edited DC Record", true);
        } catch (Exception ex) {
            logger.error("Failed to load XML content", ex);
        }
    }

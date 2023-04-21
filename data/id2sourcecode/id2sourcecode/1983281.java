    public void ingest(Context context, DSpaceObject dso, Element root) throws CrosswalkException, IOException, SQLException, AuthorizeException {
        Date timeStart = new Date();
        if (dso.getType() != Constants.ITEM) {
            throw new CrosswalkObjectNotSupported("OREIngestionCrosswalk can only crosswalk an Item.");
        }
        Item item = (Item) dso;
        if (root == null) {
            System.err.println("The element received by ingest was null");
            return;
        }
        Document doc = new Document();
        doc.addContent(root.detach());
        XPath xpathLinks;
        List<Element> aggregatedResources;
        String entryId;
        try {
            xpathLinks = XPath.newInstance("/atom:entry/atom:link[@rel=\"" + ORE_NS.getURI() + "aggregates" + "\"]");
            xpathLinks.addNamespace(ATOM_NS);
            aggregatedResources = xpathLinks.selectNodes(doc);
            xpathLinks = XPath.newInstance("/atom:entry/atom:link[@rel='alternate']/@href");
            xpathLinks.addNamespace(ATOM_NS);
            entryId = ((Attribute) xpathLinks.selectSingleNode(doc)).getValue();
        } catch (JDOMException e) {
            throw new CrosswalkException("JDOM exception occured while ingesting the ORE", e);
        }
        XPath xpathDesc;
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMinimumIntegerDigits(4);
        for (Element resource : aggregatedResources) {
            String href = resource.getAttributeValue("href");
            log.debug("ORE processing: " + href);
            String bundleName;
            Element desc = null;
            try {
                xpathDesc = XPath.newInstance("/atom:entry/oreatom:triples/rdf:Description[@rdf:about=\"" + this.encodeForURL(href) + "\"][1]");
                xpathDesc.addNamespace(ATOM_NS);
                xpathDesc.addNamespace(ORE_ATOM);
                xpathDesc.addNamespace(RDF_NS);
                desc = (Element) xpathDesc.selectSingleNode(doc);
            } catch (JDOMException e) {
                e.printStackTrace();
            }
            if (desc != null && desc.getChild("type", RDF_NS).getAttributeValue("resource", RDF_NS).equals(DS_NS.getURI() + "DSpaceBitstream")) {
                bundleName = desc.getChildText("description", DCTERMS_NS);
                log.debug("Setting bundle name to: " + bundleName);
            } else {
                log.info("Could not obtain bundle name; using 'ORIGINAL'");
                bundleName = "ORIGINAL";
            }
            Bundle[] targetBundles = item.getBundles(bundleName);
            Bundle targetBundle;
            if (targetBundles.length == 0) {
                targetBundle = item.createBundle(bundleName);
                item.addBundle(targetBundle);
            } else {
                targetBundle = targetBundles[0];
            }
            URL ARurl = null;
            InputStream in = null;
            if (href != null) {
                try {
                    String processedURL = encodeForURL(href);
                    ARurl = new URL(processedURL);
                    in = ARurl.openStream();
                } catch (FileNotFoundException fe) {
                    log.error("The provided URI failed to return a resource: " + href);
                } catch (ConnectException fe) {
                    log.error("The provided URI was invalid: " + href);
                }
            } else {
                throw new CrosswalkException("Entry did not contain link to resource: " + entryId);
            }
            if (in != null) {
                Bitstream newBitstream = targetBundle.createBitstream(in);
                String bsName = resource.getAttributeValue("title");
                newBitstream.setName(bsName);
                String mimeString = resource.getAttributeValue("type");
                BitstreamFormat bsFormat = BitstreamFormat.findByMIMEType(context, mimeString);
                if (bsFormat == null) {
                    bsFormat = FormatIdentifier.guessFormat(context, newBitstream);
                }
                newBitstream.setFormat(bsFormat);
                newBitstream.update();
                targetBundle.addBitstream(newBitstream);
                targetBundle.update();
            } else {
                throw new CrosswalkException("Could not retrieve bitstream: " + entryId);
            }
        }
        log.info("OREIngest for Item " + item.getID() + " took: " + (new Date().getTime() - timeStart.getTime()) + "ms.");
    }

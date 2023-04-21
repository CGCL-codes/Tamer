    public static void searchResults(String name) throws SQLException, IOException, Exception {
        try {
            Statement termsQuery;
            termsQuery = con.createStatement();
            Statement resultsQuery;
            resultsQuery = con.createStatement();
            ResultSet searchTermsRS = null;
            searchTermsRS = termsQuery.executeQuery("SELECT t.terms from uterms t, user u WHERE t.id = u.id and u.username = '" + name + "' AND t.active = 1");
            while (searchTermsRS.next()) {
                ResultSet searchResultsRS = null;
                searchResultsRS = resultsQuery.executeQuery("SELECT a.feedlink, b.url, b.title, b.summary, MATCH( b.title, b.summary) AGAINST ('" + searchTermsRS.getString(1) + "') AS score FROM feeds a, feeddata b WHERE MATCH (b.title, b.summary) AGAINST ('" + searchTermsRS.getString(1) + "' IN BOOLEAN MODE) GROUP BY score ORDER BY score DESC LIMIT 10");
                while (searchResultsRS.next()) {
                    Statement getMaxIdStatement;
                    getMaxIdStatement = con.createStatement();
                    ResultSet rs = getMaxIdStatement.executeQuery("SELECT id FROM objectId");
                    String ObjectID = null;
                    while (rs.next()) {
                        ObjectID = rs.getString(1);
                    }
                    rurl = searchResultsRS.getString(1);
                    rtitle = searchResultsRS.getString(3);
                    rsummary = searchResultsRS.getString(4);
                    rterm = searchTermsRS.getString(1);
                    FileWriter fstream = new FileWriter("/tmp/" + name + ".outputfile.txt", false);
                    BufferedWriter out = new BufferedWriter(fstream);
                    out.write("Username: " + name + "\n");
                    out.write("URL: " + rurl + "\n");
                    out.write("Title: " + rtitle + "\n");
                    out.write("Summary: " + rsummary + "\n");
                    out.write("Matched on: " + rterm + "\n");
                    out.close();
                    FedoraClient client = new FedoraClient("http://mark.luntzel.com:8280/fedora", "fedoraAdmin", "fedoraAdmin");
                    FedoraAPIM apim = client.getAPIM();
                    Element e = null;
                    Document xmldoc = new DocumentImpl();
                    Element root = xmldoc.createElement("foxml:digitalObject");
                    root.setAttribute("VERSION", "1.1");
                    root.setAttribute("xmlns:foxml", "info:fedora/fedora-system:def/foxml#");
                    root.setAttribute("PID", "feedfilter:" + ObjectID);
                    root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
                    root.setAttribute("xsi:schemaLocation", "info:fedora/fedora-system:def/foxml# http://www.fedora.info/definitions/1/0/foxml1-1.xsd");
                    String[] NAME = { "http://www.w3.org/1999/02/22-rdf-syntax-ns#type", "info:fedora/fedora-system:def/model#state", "info:fedora/fedora-system:def/model#label" };
                    String[] VALUE = { "FedoraObject", "B", "FeedFilter Search Result Object" };
                    Element objproperties = xmldoc.createElement("foxml:objectProperties");
                    for (int i = 0; i < NAME.length; i++) {
                        e = xmldoc.createElement("foxml:property");
                        e.setAttributeNS(null, "NAME", NAME[i]);
                        e.setAttributeNS(null, "VALUE", VALUE[i]);
                        objproperties.appendChild(e);
                    }
                    root.appendChild(objproperties);
                    xmldoc.appendChild(root);
                    String filename = "/tmp/abc.xml";
                    FileOutputStream fos = new FileOutputStream(filename);
                    OutputFormat of = new OutputFormat("XML", "UTF-8", true);
                    of.setIndent(1);
                    of.setIndenting(true);
                    XMLSerializer serializer = new XMLSerializer(fos, of);
                    serializer.asDOMSerializer();
                    serializer.serialize(xmldoc.getDocumentElement());
                    fos.close();
                    ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                    File ingestFile = new File(filename);
                    FileInputStream inStream = new FileInputStream(ingestFile);
                    pipeStream(inStream, out2, 4096);
                    String pid = apim.ingest(out2.toByteArray(), "info:fedora/fedora-system:FOXML-1.1", "FeedFilter Object");
                    System.out.println("SOAP Request: ingest " + ObjectID);
                    System.out.println("SOAP Response: pid = " + pid);
                    String tempURI = client.uploadFile(new File("/tmp/" + name + ".outputfile.txt"));
                    apim.addDatastream(pid, "FeedFilterDS", new String[] {}, "FeedFilterDS", true, "text/plain", null, tempURI, "M", "A", null, null, "Feed Filter DS");
                    incrementObjectID();
                    System.out.println();
                }
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

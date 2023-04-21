    public static void main(String[] args) {
        String listfile = args[0];
        String resultfile = args[1];
        Hashtable topmatches = new Hashtable();
        ArrayList index = new ArrayList();
        HashSet cvs = new HashSet();
        int listtotal = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(listfile));
            while (reader.ready()) {
                String line = reader.readLine();
                topmatches.put(line, new ArrayList());
                index.add(line);
                listtotal++;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(resultfile));
            while (reader.ready()) {
                String[] line = reader.readLine().split("\t");
                if (topmatches.containsKey(line[0])) {
                    ArrayList top = (ArrayList) topmatches.get(line[0]);
                    top.add(new TopMatch(line[1], line[2], line[3], line[4], line[5], line[6]));
                } else {
                    System.out.println(line[0] + " not found in list.");
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        int found = 0;
        int allfound = 0;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element root = (Element) document.createElement("results");
            document.appendChild(root);
            Iterator it = index.iterator();
            while (it.hasNext()) {
                String query = (String) it.next();
                Element qElem = document.createElement("query");
                qElem.setAttribute("name", query);
                root.appendChild(qElem);
                ArrayList top = (ArrayList) topmatches.get(query);
                if (top.size() > 0) {
                    Collections.sort(top);
                    qElem.setAttribute("length", ((TopMatch) top.get(0)).queryLength + "");
                    found++;
                    for (int i = 0; i < numbertop; i++) {
                        if (i < top.size()) {
                            TopMatch topMatch = (TopMatch) top.get(i);
                            allfound++;
                            String[] ident = topMatch.name.split("\\|");
                            Element tElem = document.createElement("target");
                            tElem.setAttribute("ondexid", ident[1]);
                            tElem.setAttribute("taxid", ident[3]);
                            tElem.setAttribute("cv", ident[5]);
                            cvs.add(ident[5]);
                            if (ident.length == 7) tElem.setAttribute("desc", ident[6]);
                            tElem.setAttribute("length", topMatch.targetLength + "");
                            qElem.appendChild(tElem);
                            Element mElem = document.createElement("match");
                            mElem.setAttribute("score", topMatch.score + "");
                            mElem.setAttribute("evalue", topMatch.evalue + "");
                            mElem.setAttribute("length", topMatch.matchLength + "");
                            tElem.appendChild(mElem);
                        }
                    }
                } else {
                    qElem.appendChild(document.createTextNode("nothing found"));
                }
            }
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            FileOutputStream stream = new FileOutputStream(resultfile + ".top.xml");
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(stream);
            transformer.transform(source, result);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerConfigurationException tce) {
            System.out.println("\n** Transformer Factory error");
            System.out.println("   " + tce.getMessage());
            Throwable x = tce;
            if (tce.getException() != null) x = tce.getException();
            x.printStackTrace();
        } catch (TransformerException te) {
            System.out.println("\n** Transformation error");
            System.out.println("   " + te.getMessage());
            Throwable x = te;
            if (te.getException() != null) x = te.getException();
            x.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        System.out.println("Total of queries in list: " + listtotal + " queries found: " + found + " total of matches: " + allfound);
        System.out.println("CVs found " + cvs);
    }

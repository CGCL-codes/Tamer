    public static void main(String result, File outfile, File sampleInputFile) {
        String rootLabel = null;
        try {
            BufferedReader reader = new BufferedReader(new StringReader(result));
            reader.readLine();
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                if (line.trim().length() == 0) continue;
                String[] parts = line.split("=");
                String relationName = parts[0];
                String relationContents = parts[1].substring(1, parts[1].length() - 1);
                System.out.println(relationName + " = " + relationContents);
                if (relationName.equals("xml/tree.root")) {
                    rootLabel = relationContents.substring(relationContents.indexOf("->") + 2);
                }
                if (relationName.equals("xml/tree.nodes")) {
                    for (String tuple : relationContents.split(",")) {
                        tuple = tuple.trim();
                        String nodeName = tuple.substring(tuple.indexOf("->") + 2);
                        System.out.println("Node: " + nodeName);
                        String elementName = nodeName.substring(0, nodeName.indexOf("["));
                        System.out.println("Element: " + elementName);
                        XMLNode newnode = new XMLNode();
                        newnode.setElementName(elementName);
                        XMLNodeDictionary.put(nodeName, newnode);
                    }
                }
                if (relationName.equals("xml/tree.fchild")) {
                    for (String tuple : relationContents.split(",")) {
                        tuple = tuple.trim();
                        String[] tupleParts = tuple.split("->");
                        if (tupleParts.length == 3) {
                            String parent = tupleParts[1];
                            String fchild = tupleParts[2];
                            System.out.println("parent: " + parent + " fchild: " + fchild);
                            XMLNode nparent = XMLNodeDictionary.get(parent);
                            XMLNode nchild = XMLNodeDictionary.get(fchild);
                            nparent.setFchild(nchild);
                        }
                    }
                }
                if (relationName.equals("xml/tree.rbrother")) {
                    if (relationContents.length() > 0) for (String tuple : relationContents.split(",")) {
                        tuple = tuple.trim();
                        String[] tupleParts = tuple.split("->");
                        String parent = tupleParts[1];
                        String rbrother = tupleParts[2];
                        System.out.println("parent: " + parent + " rbrother: " + rbrother);
                        XMLNode nparent = XMLNodeDictionary.get(parent);
                        XMLNode nbrother = XMLNodeDictionary.get(rbrother);
                        nparent.setRbrother(nbrother);
                    }
                }
            }
            XMLNode root = XMLNodeDictionary.get(rootLabel);
            System.out.println("root " + rootLabel + root);
            BufferedWriter writer = new BufferedWriter(new FileWriter(outfile));
            CsvReader csvreader = new CsvReader(sampleInputFile.getPath());
            csvreader.readHeaders();
            csvreader.readRecord();
            printTree(writer, 0, root, csvreader, sampleInputFile.getPath());
            csvreader.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

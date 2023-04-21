    public static void main(String[] args) {
        BufferedReader in = null;
        try {
            URL url = new URL("http://api.openstreetmap.org/api/0.6/map?bbox=14.59821,50.11001,14.60419,50.11411");
            URLConnection conn = url.openConnection();
            InputStream inStr = conn.getInputStream();
            conn.connect();
            XMLInputFactory inXML = XMLInputFactory.newInstance();
            XMLStreamReader reader = inXML.createXMLStreamReader(inStr);
            DefaultMutableTreeNode current = new DefaultMutableTreeNode();
            parseRestOfDocument(reader, current);
            Enumeration e = current.preorderEnumeration();
            System.out.println("root " + current.getFirstChild().getChildCount());
            TreeNode osmNode = current.getFirstChild().getChildAt(1);
            for (Enumeration en = current.getFirstChild().children(); en.hasMoreElements(); ) {
                if (osmNode.getChildCount() > 0) {
                    System.out.println(osmNode.toString());
                }
                osmNode = (TreeNode) en.nextElement();
            }
        } catch (MalformedURLException ex) {
            System.err.println(ex);
        } catch (FileNotFoundException ex) {
            System.err.println("Failed to open stream to URL: " + ex);
        } catch (IOException ex) {
            System.err.println("Error reading URL content: " + ex);
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        if (in != null) try {
            in.close();
        } catch (IOException ex) {
        }
    }

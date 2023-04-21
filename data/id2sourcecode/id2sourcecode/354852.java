    public ProducableValuableChessBoard(String fileName) throws Exception {
        this();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            System.err.println(pce);
            System.exit(1);
        }
        Document doc = null;
        try {
            doc = db.parse(fileName);
        } catch (DOMException dom) {
            System.err.println(dom.getMessage());
            System.exit(1);
        } catch (IOException ioe) {
            System.err.println(ioe);
            System.exit(1);
        }
        Element root = doc.getDocumentElement();
        String turn = root.getAttribute("turn");
        String computer = root.getAttribute("computer");
        setTurn(turn);
        Player fRed = new Player();
        fRed.setFlag("red");
        Element redPlayer = (Element) (root.getElementsByTagName("redPlayer").item(0));
        NodeList rchessmen = redPlayer.getElementsByTagName("chessman");
        for (int j = 0; j < rchessmen.getLength(); ++j) {
            Element rchessman = (Element) rchessmen.item(j);
            Chessman rcm = (Chessman) (Class.forName(rchessman.getAttribute("className")).newInstance());
            rcm.setName(rchessman.getAttribute("name"));
            rcm.setPosition(Integer.parseInt(rchessman.getAttribute("x")), Integer.parseInt(rchessman.getAttribute("y")));
            fRed.addChessman(rcm);
        }
        addPlayer(fRed);
        Player fBlue = new Player();
        fBlue.setFlag("blue");
        Element bluePlayer = (Element) (root.getElementsByTagName("bluePlayer").item(0));
        NodeList bchessmen = bluePlayer.getElementsByTagName("chessman");
        for (int j = 0; j < bchessmen.getLength(); ++j) {
            Element bchessman = (Element) bchessmen.item(j);
            Chessman bcm = (Chessman) (Class.forName(bchessman.getAttribute("className")).newInstance());
            bcm.setName(bchessman.getAttribute("name"));
            bcm.setPosition(Integer.parseInt(bchessman.getAttribute("x")), Integer.parseInt(bchessman.getAttribute("y")));
            fBlue.addChessman(bcm);
        }
        addPlayer(fBlue);
    }

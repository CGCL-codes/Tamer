    private void scrape(String ipAddress) throws MalformedURLException, IOException, ParserException {
        URL url = new URL("http://" + ipAddress + "/DeviceInformation");
        CIPPhone.log_.info(CIPPhone.messages_.getString("cipPhone.log.info.scraping") + url);
        Lexer lexer = new Lexer();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        lexer.setPage(new Page(conn));
        Node currentNode = lexer.nextNode();
        CIPPhone.log_.debug(CIPPhone.messages_.getString("cipPhone.log.debug.htmlNode") + currentNode.getText());
        while (!currentNode.getText().toUpperCase().startsWith("TITLE")) {
            currentNode = lexer.nextNode();
            CIPPhone.log_.debug(CIPPhone.messages_.getString("cipPhone.log.debug.htmlNode") + currentNode.getText());
        }
        currentNode = lexer.nextNode();
        String titleText = currentNode.getText();
        CIPPhone.log_.debug(CIPPhone.messages_.getString("cipPhone.log.debug.titleValue") + titleText);
        if (!titleText.startsWith("Cisco Systems, Inc.")) {
            CIPPhone.log_.error(CIPPhone.messages_.getString("cipPhone.log.error.notCIP.invalidTitle"));
            throw new ParserException(CIPPhone.messages_.getString("cipPhone.log.error.notCIP.invalidTitle"));
        }
        int valuesFound = 0;
        String toMatch = null;
        while (valuesFound < 3) {
            currentNode = lexer.nextNode();
            CIPPhone.log_.debug("Current node: " + currentNode.getText());
            switch(valuesFound) {
                case 0:
                    toMatch = "MAC Address";
                    CIPPhone.log_.debug(CIPPhone.messages_.getString("cipPhone.log.debug.toMatch") + toMatch);
                    break;
                case 1:
                    toMatch = "Host Name";
                    CIPPhone.log_.debug(CIPPhone.messages_.getString("cipPhone.log.debug.toMatch") + toMatch);
                    break;
                case 2:
                    toMatch = "Phone DN";
                    CIPPhone.log_.debug(CIPPhone.messages_.getString("cipPhone.log.debug.toMatch") + toMatch);
                    break;
            }
            if (currentNode.getText().equalsIgnoreCase(toMatch)) {
                CIPPhone.log_.debug(CIPPhone.messages_.getString("cipPhone.log.debug.htmlNode") + toMatch);
                currentNode = lexer.nextNode();
                CIPPhone.log_.debug(CIPPhone.messages_.getString("cipPhone.log.debug.htmlNode") + toMatch);
                currentNode = lexer.nextNode();
                CIPPhone.log_.debug(CIPPhone.messages_.getString("cipPhone.log.debug.htmlNode") + toMatch);
                currentNode = lexer.nextNode();
                CIPPhone.log_.debug(CIPPhone.messages_.getString("cipPhone.log.debug.htmlNode") + toMatch);
                currentNode = lexer.nextNode();
                CIPPhone.log_.debug(CIPPhone.messages_.getString("cipPhone.log.debug.htmlNode") + toMatch);
                currentNode = lexer.nextNode();
                CIPPhone.log_.debug(CIPPhone.messages_.getString("cipPhone.log.debug.htmlNode") + toMatch);
                currentNode = lexer.nextNode();
                CIPPhone.log_.debug(CIPPhone.messages_.getString("cipPhone.log.debug.htmlNode") + toMatch);
                currentNode = lexer.nextNode();
                CIPPhone.log_.debug(CIPPhone.messages_.getString("cipPhone.log.debug.htmlNode") + toMatch);
                currentNode = lexer.nextNode();
                CIPPhone.log_.debug(CIPPhone.messages_.getString("cipPhone.log.debug.htmlNode") + toMatch);
                currentNode = lexer.nextNode();
                CIPPhone.log_.debug(CIPPhone.messages_.getString("cipPhone.log.debug.htmlNode") + toMatch);
                if (valuesFound == 0) {
                    this.macAddress = currentNode.getText();
                } else if (valuesFound == 1) {
                    this.hostName = currentNode.getText();
                } else if (valuesFound == 2) {
                    this.phoneDN = currentNode.getText();
                }
                valuesFound++;
            }
        }
        toMatch = "Model Number";
        CIPPhone.log_.debug(CIPPhone.messages_.getString("cipPhone.log.debug.toMatch") + toMatch);
        while (!currentNode.getText().equalsIgnoreCase(toMatch)) {
            currentNode = lexer.nextNode();
        }
        CIPPhone.log_.debug(CIPPhone.messages_.getString("cipPhone.log.debug.htmlNode") + toMatch);
        currentNode = lexer.nextNode();
        CIPPhone.log_.debug(CIPPhone.messages_.getString("cipPhone.log.debug.htmlNode") + currentNode.getText());
        currentNode = lexer.nextNode();
        CIPPhone.log_.debug(CIPPhone.messages_.getString("cipPhone.log.debug.htmlNode") + currentNode.getText());
        currentNode = lexer.nextNode();
        CIPPhone.log_.debug(CIPPhone.messages_.getString("cipPhone.log.debug.htmlNode") + currentNode.getText());
        currentNode = lexer.nextNode();
        CIPPhone.log_.debug(CIPPhone.messages_.getString("cipPhone.log.debug.htmlNode") + currentNode.getText());
        currentNode = lexer.nextNode();
        CIPPhone.log_.debug(CIPPhone.messages_.getString("cipPhone.log.debug.htmlNode") + currentNode.getText());
        currentNode = lexer.nextNode();
        CIPPhone.log_.debug(CIPPhone.messages_.getString("cipPhone.log.debug.htmlNode") + currentNode.getText());
        currentNode = lexer.nextNode();
        CIPPhone.log_.debug(CIPPhone.messages_.getString("cipPhone.log.debug.htmlNode") + currentNode.getText());
        currentNode = lexer.nextNode();
        CIPPhone.log_.debug(CIPPhone.messages_.getString("cipPhone.log.debug.htmlNode") + currentNode.getText());
        currentNode = lexer.nextNode();
        CIPPhone.log_.debug(CIPPhone.messages_.getString("cipPhone.log.debug.htmlNode") + currentNode.getText());
        CIPPhone.log_.debug(CIPPhone.messages_.getString("cipPhone.log.debug.htmlNode") + currentNode.getText());
        this.modelNumber = currentNode.getText();
    }

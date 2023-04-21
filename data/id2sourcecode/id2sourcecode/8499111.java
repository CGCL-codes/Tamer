    public void parse(String response) {
        if (Whisper.isDebugging()) {
            TextStyle.addInformationalText(response, Whisper.getClient().getChannel(Whisper.TAB_RAW));
        }
        XMLDocument xDoc = null;
        try {
            xDoc = XMLDocument.parse(response);
        } catch (XMLParseException ex) {
            if (Whisper.isDebugging()) {
                ex.printStackTrace();
            }
        }
        if (xDoc != null) {
            String reply = xDoc.getRootNode().getChild("Reply").getValue();
            if (reply != null) {
                Response r = Responses.InstantiateResponse(reply);
                if (r != null) {
                    r.action(xDoc, chat.getConnection());
                }
            }
        }
    }

    public void run() {
        log.debug("run: START");
        if (quote == null) {
            log.warn("run: input quote is null");
            return;
        }
        log.debug("run: retrieve data for code[" + quote.getStockCode() + "]");
        Parser parser;
        try {
            parser = new Parser();
            NodeFilter filterTD = new TagNameFilter("TD");
            NodeFilter filterSPAN = new TagNameFilter("SPAN");
            NodeFilter orFilter = new OrFilter(new NodeFilter[] { filterTD, filterSPAN });
            HttpURLConnection connection;
            URL url = new URL(ETNET_QUOTE_URL + quote.getStockCode());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0; Trident/5.0)");
            parser.setFeedback(Parser.STDOUT);
            parser.setConnection(connection);
            NodeList list = parser.parse(orFilter);
            for (Node n : list.toNodeArray()) {
                if (n.getText().contains("Quote")) {
                    if (n.getFirstChild() != null) {
                        if (n.getFirstChild().getText().contains("Last Updated:")) {
                            quote.setLastUpdate(n.getChildren().elementAt(2).getFirstChild().getText());
                        }
                        if (n.getFirstChild().getText().contains("src=\"../../common/images/arrow")) {
                            quote.setPrice(n.getChildren().elementAt(1).getText());
                            quote.setChange(n.getNextSibling().getNextSibling().getNextSibling().getFirstChild().getText());
                        }
                        if (n.getChildren().size() > 3 && n.getFirstChild().getText().contains("High") && n.getChildren().elementAt(2).getText().contains("RT")) {
                            Node child = n.getParent().getParent().getNextSibling().getNextSibling().getChildren().elementAt(1).getFirstChild().getFirstChild();
                            while (child != null) {
                                try {
                                    Double.parseDouble(child.getText());
                                    quote.setHigh(child.getText());
                                    break;
                                } catch (NumberFormatException e) {
                                    log.error("Not number [" + child.getText() + "]", e);
                                    child = child.getFirstChild();
                                }
                            }
                        }
                        if (n.getChildren().size() > 3 && n.getFirstChild().getText().contains("Low") && n.getChildren().elementAt(2).getText().contains("RT")) {
                            Node child = n.getParent().getParent().getNextSibling().getNextSibling().getChildren().elementAt(1).getFirstChild().getFirstChild();
                            while (child != null) {
                                try {
                                    Double.parseDouble(child.getText());
                                    quote.setLow(child.getText());
                                    break;
                                } catch (NumberFormatException e) {
                                    log.error("Not number [" + child.getText() + "]", e);
                                    child = child.getFirstChild();
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Error when retrieve data from ETNET", e);
        }
        log.debug("run: END");
    }

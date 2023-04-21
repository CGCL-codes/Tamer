    public WmlDeck startParse() {
        if (sourceDeck != null) {
            try {
                url = sourceDeck.expandVariables(url);
            } catch (java.io.IOException E) {
                System.err.println("caught exception:" + E.toString());
            }
        } else {
            System.err.println("No expansion required");
        }
        try {
            sourceUrl = new URL(url);
        } catch (java.net.MalformedURLException E) {
            sourceUrl = null;
        }
        wmlelements = new Stack();
        attributes = new Hashtable();
        deck = null;
        System.err.println("URL:[" + sourceUrl.toString() + "]");
        try {
            XmlParser parser = new XmlParser();
            parser.setHandler(this);
            try {
                URL url = new URL(gateway + sourceUrl.toString());
                URLConnection connection = url.openConnection();
                if (sourceTask != null) {
                    connection.setDoOutput(true);
                    PrintWriter out = new PrintWriter(connection.getOutputStream());
                    out.write(sourceTask.getparams());
                    out.close();
                }
                InputStream is = connection.getInputStream();
                rawWML = new ByteArrayOutputStream();
                int r;
                while ((r = is.read()) > -1) {
                    rawWML.write(r);
                }
                is.close();
                rawWML.close();
                connection = null;
                url = null;
            } catch (java.io.IOException E) {
                System.err.println("Error:Cannot retrieve URL");
                String ioerr = "<wml><card title=\"Error!\"><p>The requested URL " + "<!--[" + url.toString() + "]-->" + "could not be retrieved.</p></card></wml>";
                rawWML = new ByteArrayOutputStream();
                rawWML.write(ioerr.getBytes());
            } catch (Exception E) {
                System.err.println("Error:" + E.toString());
                throw (E);
            }
            try {
                parser.parse(url, null, new ByteArrayInputStream(rawWML.toByteArray()), (String) null);
            } catch (UTFDataFormatException E) {
                parser.parse(url, null, new ByteArrayInputStream(rawWML.toByteArray()), "ISO-8859-1");
                System.err.println("WARNING:Encoding MAY be ISO-8859-1");
            }
            System.err.println("[ok]");
        } catch (java.io.FileNotFoundException e) {
            deck = new WmlDeck(sourceUrl);
            WmlCard ioerr = new WmlCard(deck, "file_not_found");
            ioerr.cardData("<center><b>Error!</b></center>" + url + "<br>not found.<br>");
            deck.addCard(ioerr);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[not ok]" + e.getMessage());
            logFatalError("fatal=" + sourceUrl);
            deck = new WmlDeck(sourceUrl);
            WmlCard wmlerr = new WmlCard(deck, "wml_error");
            wmlerr.cardData("<center><b>Error!</b></center>" + url + "<br>may contain errors.<br>");
            deck.addCard(wmlerr);
        }
        if (deck != null) {
            deck.url = url;
            deck.wml(rawWML.toString());
        }
        return deck;
    }

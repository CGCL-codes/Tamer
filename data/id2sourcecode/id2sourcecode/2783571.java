    private Wrapper(URL url) {
        resultWrapper_ = new ResultWrapper();
        beginOfResult_ = null;
        endOfResult_ = null;
        try {
            Tidy tidy = new Tidy();
            tidy.setShowWarnings(false);
            tidy.setQuiet(true);
            InputStream is = url.openStream();
            System.out.print("Retrieving page...");
            String thePage = getString(new BufferedReader(new InputStreamReader(is)));
            System.out.print("done \nparsing...");
            NodeList body = tidy.parseDOM(new StringBufferInputStream(thePage), null).getElementsByTagName("body");
            NodeList nl = body.item(0).getChildNodes();
            System.out.print("done \nfinding train...");
            Date start = new Date();
            PatternSequence ps = new PatternSequence();
            ps.findSequence(nl);
            Train train = ps.getBestTrain();
            train.printTrain();
            System.out.print("done \nmatching train...");
            train.matchWith(thePage);
            System.out.println("done");
            Vector textPos = train.getPositionsInText();
            if (textPos != null && textPos.size() > 3) {
                for (int i = 1; i < textPos.size(); i++) {
                    System.out.println("\n====\n" + thePage.substring(((Integer) textPos.elementAt(i - 1)).intValue(), ((Integer) textPos.elementAt(i)).intValue()) + "\n====\n");
                }
                System.out.println(textPos.size());
                textPos.remove(0);
                beginOfResult_ = findCommonBegin(thePage, textPos);
                endOfResult_ = findCommonEnd(thePage, textPos);
                System.out.println("pre:  [" + beginOfResult_ + "]");
                System.out.println("post: [" + endOfResult_ + "]");
            } else if (textPos != null) {
                System.out.println("Train smaller than 4");
            } else {
                System.out.println("no train found");
            }
        } catch (MalformedURLException ex) {
            System.out.println("malformed url given:\n" + ex);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

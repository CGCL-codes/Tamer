    private int retrieveTotal(ProgressDialog progress) {
        int offset = 6000;
        int greaterThan = 0;
        int lessThan = Integer.MAX_VALUE;
        int iteration = 0;
        while (total < 0) {
            progress.setText("Fetching total (" + ++iteration + ")");
            String queryString = "http://backend.deviantart.com/rss.xml?q=" + search.toString() + ":" + user + "&type=deviation&offset=" + offset;
            GetMethod method = new GetMethod(queryString);
            try {
                int sc = -1;
                do {
                    sc = client.executeMethod(method);
                    if (sc != 200) {
                        LoggableException ex = new LoggableException(method.getResponseBodyAsString());
                        Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), ex);
                        int res = DialogHelper.showConfirmDialog(owner, "An error has occured when contacting deviantART : error " + sc + ". Try again?", "Continue?", JOptionPane.YES_NO_OPTION);
                        if (res == JOptionPane.NO_OPTION) {
                            return -1;
                        }
                    }
                } while (sc != 200);
                XmlToolkit toolkit = XmlToolkit.getInstance();
                Element responses = toolkit.parseDocument(method.getResponseBodyAsStream());
                method.releaseConnection();
                List<?> deviations = toolkit.getMultipleNodes(responses, "channel/item");
                HashMap<String, String> prefixes = new HashMap<String, String>();
                prefixes.put("atom", responses.getOwnerDocument().lookupNamespaceURI("atom"));
                Node next = toolkit.getSingleNode(responses, "channel/atom:link[@rel='next']", toolkit.getNamespaceContext(prefixes));
                int size = deviations.size();
                if (debug) {
                    System.out.println();
                    System.out.println();
                    System.out.println("Lesser  Than: " + lessThan);
                    System.out.println("Greater Than: " + greaterThan);
                    System.out.println("Offset: " + offset);
                    System.out.println("Size: " + size);
                }
                if (size != OFFSET && size > 0) {
                    if (next != null) {
                        greaterThan = offset + OFFSET;
                    } else {
                        if (debug) System.out.println("Total (offset + size) : " + (offset + size));
                        return offset + size;
                    }
                }
                if (size == OFFSET) {
                    greaterThan = offset + OFFSET;
                }
                if (size == 0) {
                    lessThan = offset;
                }
                if (greaterThan == lessThan) {
                    if (debug) System.out.println("Total (greaterThan) : " + greaterThan);
                    return greaterThan;
                }
                if (lessThan == Integer.MAX_VALUE) {
                    offset = offset * 2;
                } else {
                    offset = (greaterThan + lessThan) / 2;
                    if (offset % 60 != 0) {
                        offset = (offset / 60) * 60;
                    }
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
            } catch (IOException e) {
            }
        }
        return total;
    }

    public void run() {
        try {
            URL url = new URL(this.path);
            this.path = url.getProtocol() + "://" + url.getHost() + url.getPath().substring(0, (url.getPath().lastIndexOf('/') != -1 ? url.getPath().lastIndexOf('/') : url.getPath().length()));
            URLConnection con = url.openConnection();
            Parser parser = new Parser(con);
            readJavaScript(parser, url);
            updateJSFunctions();
            con = url.openConnection();
            parser = new Parser(con);
            NodeFilter[] nf = new NodeFilter[2];
            nf[0] = new TagNameFilter("a");
            nf[1] = new HasAttributeFilter("onclick");
            NodeList nlist = parser.extractAllNodesThatMatch(new OrFilter(nf));
            SimpleNodeIterator i = nlist.elements();
            while (i.hasMoreNodes()) {
                Node node = i.nextNode();
                if (node instanceof Tag) {
                    Tag tag = (Tag) node;
                    String val = tag.getAttribute("onclick");
                    if (FindHTMLTag.isNullEmptyString(val)) {
                        val = tag.getAttribute("href");
                        if (!FindHTMLTag.isNullEmptyString(val)) {
                            jsFuncAction(val);
                        }
                    } else {
                        jsFuncAction(val);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        GetLinksEvent evt = new GetLinksEvent(this.getLinks());
        this.fireGetLinksEvent(evt);
    }

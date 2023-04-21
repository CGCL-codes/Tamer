    private Collection<String> loadIndex(URL url, boolean dir) throws IOException {
        System.out.println("Loading Index of " + url.toString());
        SAXReader reader = new SAXReader(new HtmlSaxParser());
        Document document = null;
        try {
            document = reader.read(url.openStream());
        } catch (DocumentException e) {
            throw new IOException(e.getMessage());
        }
        List<Node> linkList = dir ? document.selectNodes("//a[((not (starts-with(@href, '/'))) and (preceding-sibling::img/@alt='[DIR]'))]") : document.selectNodes("//a[not (starts-with(@href, '/'))]");
        List<String> pluginLinkList = new LinkedList<String>();
        for (Node node : linkList) {
            String href = node.valueOf("@href");
            if (href != null && href.length() > 1) pluginLinkList.add(href);
        }
        return pluginLinkList;
    }

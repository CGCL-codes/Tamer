    @Override
    public String getString() {
        XML root = new XML("rdf:RDF");
        root.addAttribute("xmlns:rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        root.addAttribute("xmlns", "http://purl.org/rss/1.0/");
        root.addAttribute("xmlns:dc", "http://purl.org/dc/elements/1.1/");
        root.addAttribute("xmlns:wiki", "http://purl.org/rss/1.0/modules/wiki/");
        root.addElement(getChannelElement());
        addItemList(root);
        root.setPrettyPrint(true);
        return root.toString();
    }

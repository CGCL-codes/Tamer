    public String toString() {
        XML document = new XML("rss");
        document.setPrettyPrint(true);
        document.addAttribute("version", "2.0");
        document.addAttribute("xmlns:dc", "http://purl.org/dc/elements/1.1/");
        document.addElement(getChannelXML());
        return document.toString();
    }

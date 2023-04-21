    public static void main(String[] args) throws MalformedURLException, IOException, SAXException {
        URL url = new URL("http://www.google.com");
        InputStream in = url.openConnection().getInputStream();
        MyDOMParser parser = createDOMParser();
        parser.parse(new InputSource(in));
        HTMLDocument document = (HTMLDocument) parser.getDocument();
        in.close();
        HTMLCollection link = document.getLinks();
        int length = link.getLength();
        for (int i = 0; i < length; i++) {
            System.out.println(i + ": " + link.item(i).getAttributes().getNamedItem("href"));
        }
        System.out.println(length);
    }

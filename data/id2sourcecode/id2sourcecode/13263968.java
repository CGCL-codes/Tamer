    public void testIteratorWithAttributes() throws Exception {
        String xml = "<root><status veld=\"veldValue\">ok</status><tabs>" + "<tab id=\"getArticles\" rating=\"1\">Articles</tab>" + "<tab id=\"getFavourites\">Favourites</tab>" + "<tab id=\"getChannelsWithNews\">Channels with news</tab>" + "<tabdusniet>dusniet</tabdusniet>" + "</tabs>" + "<tab rating=\"1\">Losse tab</tab>" + "</root>";
        XMLHandler handler = getXmlHandler();
        XMLElement root = handler.parse(new StringReader(xml));
        XMLElementIterator it = new XMLElementIterator(root, "tab:id=getArticles");
        while (it.hasNext()) {
            XMLElement xmlElement = (XMLElement) it.next();
            assertTrue(xmlElement.getValue().equals("Articles"));
        }
        it = new XMLElementIterator(root, "tab:id=fietsen");
        assertFalse(it.hasNext());
        it = new XMLElementIterator(root, "tab:rating=1");
        StringBuffer sb = new StringBuffer();
        while (it.hasNext()) {
            XMLElement xmlElement = (XMLElement) it.next();
            sb.append(xmlElement.getFullName()).append(" value = ").append(xmlElement.getValue()).append(",");
        }
        assertTrue(sb.toString().equals("/root/tabs/tab value = Articles,/root/tab value = Losse tab,"));
    }

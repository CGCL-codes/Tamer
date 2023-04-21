    private static Collection<String> getClasspathEntriesFromProject(final Bundle bundle) throws Exception {
        assert bundle != null;
        final URL url = bundle.getEntry(".classpath");
        if (url == null) {
            return Collections.emptyList();
        }
        final List<String> classpathEntries = new ArrayList<String>();
        final InputStream is = url.openStream();
        try {
            final InputSource inputSource = new InputSource(is);
            final XPath xpath = XPathFactory.newInstance().newXPath();
            final String expression = "/classpath/classpathentry[@kind='output']";
            final NodeList nodes = (NodeList) xpath.evaluate(expression, inputSource, XPathConstants.NODESET);
            for (int index = 0; index < nodes.getLength(); ++index) {
                final Element element = (Element) nodes.item(index);
                final String classpathEntry = element.getAttribute("path");
                if (classpathEntry != null) {
                    classpathEntries.add(classpathEntry);
                }
            }
        } finally {
            is.close();
        }
        return Collections.unmodifiableList(classpathEntries);
    }

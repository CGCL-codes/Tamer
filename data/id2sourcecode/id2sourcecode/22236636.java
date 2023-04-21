    public List<Element> getElementsByPath(final ModelGeneratorContext context, final Element root, final String... path) {
        if (path.length <= 0) {
            return Arrays.asList(context.getRealElement(root));
        }
        List<Element> res = new ArrayList<Element>();
        String[] nextPath = new String[path.length - 1];
        for (int i = 0; i < nextPath.length; i++) {
            nextPath[i] = path[i + 1];
        }
        for (Element child : getXmlManager().getElements(path[0], context.getRealElement(root).getChildNodes())) {
            res.addAll(getElementsByPath(context, child, nextPath));
        }
        return res;
    }

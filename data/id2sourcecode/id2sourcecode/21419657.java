    public void loadExternals(Node externals) throws IOException {
        Node item = externals.getFirstChild();
        while (item != null) {
            if (item.getNodeName().equals("item")) {
                if (getAttribute(item, "type").equals("material")) {
                    String file = getAttribute(getChildNode(item, "file"), "name");
                    MaterialLoader matloader = new MaterialLoader();
                    URL url = ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE, file);
                    logger.fine("Loading materials from '" + url + "'...");
                    if (url != null) {
                        InputStream in = url.openStream();
                        matloader.load(in);
                        in.close();
                        materials.putAll(matloader.getMaterials());
                    }
                }
            }
            item = item.getNextSibling();
        }
        logger.fine("Loaded materials.  Now have: " + materials.keySet());
    }

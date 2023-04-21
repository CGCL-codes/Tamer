    private void loadUpdateDescriptors() throws XMLException, IOException {
        URI urlDoc = url.resolve(updateDoc);
        HttpURLConnection conn = (HttpURLConnection) urlDoc.toURL().openConnection();
        conn.setDoInput(true);
        this.updates = XMLUtils.read(new BufferedInputStream((InputStream) conn.getContent()));
        conn.disconnect();
        moduleUpdatesCache = new HashMap<String, Map<UpdateId, ModuleUpdateDescriptor>>();
        List<Node> lst = XMLUtils.findChildren(this.updates.getFirstChild(), "module");
        if (lst.size() == 0) {
            return;
        }
        try {
            this.bundle = new MessageRepository("updates", Locale.getDefault(), url.toURL());
        } catch (Exception e) {
            logger.error(e);
        }
        Node n;
        String moduleName;
        Module module;
        for (Iterator<Node> iter = lst.iterator(); iter.hasNext(); ) {
            n = iter.next();
            moduleName = XMLUtils.findAttribute(n, "name").getValue();
            List<Node> lst1 = XMLUtils.findChildren(n, "update");
            for (Iterator<Node> iter1 = lst1.iterator(); iter1.hasNext(); ) {
                n = iter1.next();
                ModuleUpdateDescriptor mud = new ModuleUpdateDescriptor(moduleName);
                mud.fromXML(n);
                if (this.bundle != null) {
                    mud.setUpdateDescription(this.bundle.getMessage(mud.getUpdateDescription()));
                }
                module = getRegisteredModule(moduleName);
                if (module != null && mud.isForModule(module)) {
                    Map<UpdateId, ModuleUpdateDescriptor> m = moduleUpdatesCache.get(moduleName);
                    if (m == null) {
                        m = new HashMap<UpdateId, ModuleUpdateDescriptor>();
                        moduleUpdatesCache.put(moduleName, m);
                    }
                    m.put(mud.getUpdateId(), mud);
                }
            }
        }
    }

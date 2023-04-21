    void indexJAR(EditPlugin plugin) throws IOException {
        String className = plugin.getClassName();
        String name = jEdit.getProperty("plugin." + className + ".name");
        Field classField = new Field("className", className, Store.YES, Index.NO);
        Field nameField = new Field("name", name, Store.YES, Index.NO);
        Field groupField = new Field("group", "plugins", Store.YES, Index.NO);
        Field[] fields = new Field[] { classField, nameField, groupField };
        IndexModifier pluginIndex = getWriter("help");
        Term t = new Term("name", name);
        pluginIndex.deleteDocuments(t);
        File file = plugin.getPluginJAR().getFile();
        JarFile jarFile = new JarFile(file);
        Enumeration<JarEntry> files = jarFile.entries();
        while (files.hasMoreElements()) {
            JarEntry entry = files.nextElement();
            String fileName = entry.getName();
            if (isDoc(fileName)) {
                String url = "jeditresource:/" + MiscUtilities.getFileName(jarFile.getName()) + "!/" + fileName;
                Reader reader = new InputStreamReader(new URL(url).openStream());
                Field contentField = new Field("content", reader);
                Field urlField = new Field("url", url, Store.YES, Index.NO);
                Document doc = new Document();
                for (Field f : fields) doc.add(f);
                doc.add(contentField);
                doc.add(urlField);
                pluginIndex.addDocument(doc);
            }
        }
    }

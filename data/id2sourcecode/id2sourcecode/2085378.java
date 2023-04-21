    protected void setUp() throws Exception {
        Document configuration = getConfiguration();
        Element fixtureElement = configuration.getRootElement();
        String isolation = fixtureElement.getAttributeValue("isolation");
        if ("unique".equals(isolation)) {
            String basename = fixtureElement.getAttributeValue("basename");
            if (basename == null || basename.length() < 3) {
                basename = System.getProperty("user.name");
                if (basename == null || basename.length() < 3) {
                    basename = "tmp";
                }
            }
            fixture = fixture.createUniqueDirectory(basename);
        } else if ("clean".equals(isolation)) {
            fixture.doTeardown();
        } else if ("reuse".equals(isolation)) {
        }
        createFiles(fixture, fixtureElement);
        fixtures.put("~base", fixture);
        Elements datasets = fixtureElement.getChildElements("data");
        for (int i = 0; i < datasets.size(); ++i) {
            Element dataElement = datasets.get(i);
            data.put(dataElement.getAttributeValue("id"), dataElement);
        }
        Elements beansElements = fixtureElement.getChildElements("beans");
        for (int i = 0; i < beansElements.size(); ++i) {
            Element beansElement = beansElements.get(i);
            Element root = (Element) beansElement.copy();
            root.removeAttribute(root.getAttribute("id"));
            Document beansDoc = new Document(root);
            String publicid = "-//SPRING//DTD BEAN//EN";
            String systemid = "http://www.springframework.org/dtd/spring-beans.dtd";
            DocType doctype = new DocType(beansDoc.getRootElement().getLocalName(), publicid, systemid);
            beansDoc.setDocType(doctype);
            byte[] beansBytes = beansDoc.toXML().getBytes();
            Resource r = new InputStreamResource(new ByteArrayInputStream(beansBytes), "");
            XmlBeanFactory bf = new XmlBeanFactory(r);
            configurer.postProcessBeanFactory(bf);
            beans.put(beansElement.getAttributeValue("id"), bf);
        }
        Elements jarElements = fixtureElement.getChildElements("jar");
        for (int i = 0; i < jarElements.size(); ++i) {
            Element jarElement = jarElements.get(i);
            Element manifestElement = jarElement.getChildElements("manifest").get(0);
            String manifestText = manifestElement.getValue().trim() + "\n";
            InputStream mis = new ByteArrayInputStream(manifestText.getBytes());
            Manifest manifest = new Manifest(mis);
            File jarFile = createFile(fixture, jarElement);
            JarOutputStream jout = new JarOutputStream(new FileOutputStream(jarFile), manifest);
            Elements entryElements = jarElement.getChildElements("file");
            for (int j = 0; j < entryElements.size(); ++j) {
                Element entryElement = entryElements.get(j);
                File entryFile = createFile(fixture, entryElement);
                JarEntry entry = new JarEntry(entryFile.getName());
                jout.putNextEntry(entry);
                jout.write(DirectoryFixture.readBytes(entryFile));
                jout.closeEntry();
            }
            jout.close();
        }
        if ("false".equals(fixtureElement.getAttributeValue("cleanup"))) {
            cleanup = false;
        }
    }

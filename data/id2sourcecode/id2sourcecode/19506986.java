    public void prepereMethod(File javaFile, String packageName, String serviceName, TestPair tp) throws DocumentException, InputXMLAlreadyExistsException, DuplicatedTestMethodException, InGeneretingException {
        File xmlFile = new File(tp.getXmlFilePath());
        xmlFile.deleteOnExit();
        DocumentSource xmlDoc = null;
        TransformerFactory factory = TransformerFactory.newInstance();
        DocumentSource src = null;
        Transformer transformer = null;
        RandomAccessFile raf = null;
        RuntimeException rte = null;
        try {
            xmlDoc = new DocumentSource(new SAXReader().read(xmlFile));
            InputStream xslFile = getClass().getClassLoader().getResourceAsStream("xslt/tomethod.xsl");
            src = new DocumentSource(new SAXReader().read(xslFile));
            transformer = factory.newTransformer(src);
            String name = tp.getMethodName();
            if (name == null || "".equals(name)) {
                throw new InGeneretingException("Name of method can not be null or empty string.");
            }
            if (isMethodNameUsed(javaFile, name)) {
                throw new DuplicatedTestMethodException(packageName, serviceName, name, "Method " + packageName + ":" + serviceName + "." + name + " is already used");
            }
            raf = new RandomAccessFile(javaFile, "rw");
            raf.seek(raf.length() - 1);
            while (raf.read() != '}') {
                raf.seek(raf.getFilePointer() - 2);
            }
            raf.seek(raf.getFilePointer() - 2);
            Writer wr = Channels.newWriter(raf.getChannel(), "UTF-8");
            wr.write("\n\n\t/**\n\t * Test .");
            wr.write("\n\t * ");
            wr.write("\n\t * @throws Throwable");
            wr.write("\n\t */");
            wr.write("\n\tpublic void " + name + "()\n\t\t\tthrows Throwable \n");
            wr.write("\t{\n");
            File inputDocFile = new File("tempInDoc.xml");
            inputDocFile.deleteOnExit();
            StreamResult result = new StreamResult(inputDocFile);
            transformer.transform(xmlDoc, result);
            writeInputXml(wr, inputDocFile, javaFile.getAbsolutePath(), xmlFile.getName(), packageName);
            writeTryCatchPart(wr, packageName, serviceName);
            wr.write("\n\t}\n");
            wr.write("}\n");
            wr.flush();
        } catch (TransformerException e1) {
            rte = new RuntimeException("XSL transformation failed!");
            rte.initCause(e1);
        } catch (FileNotFoundException e) {
            rte = new RuntimeException("Java file not found");
            rte.initCause(e);
        } catch (MalformedURLException e) {
            rte = new RuntimeException("Wrong xsl document location");
            rte.initCause(e);
        } catch (IOException e) {
            rte = new RuntimeException("IO problem!");
            rte.initCause(e);
        } finally {
            try {
                if (raf != null) {
                    raf.close();
                }
                if (rte != null) {
                    throw rte;
                }
            } catch (IOException e2) {
                rte = new RuntimeException("NO xsl document!");
                rte.initCause(e2);
                throw rte;
            }
        }
    }

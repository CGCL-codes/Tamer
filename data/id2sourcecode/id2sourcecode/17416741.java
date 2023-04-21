    public URL loadData(URL url) throws IOException, SAXException {
        if (!loading.containsKey(url)) {
            loading.put(url, null);
            OwlOntology ont = null;
            InputStream inStream = url.openStream();
            Document doc = docBuild.parse(inStream);
            Element root = doc.getDocumentElement();
            NodeList nodes = root.getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node instanceof Element) {
                    Element elem = (Element) node;
                    String tagName = elem.getTagName();
                    if (tagName.equals("owl:Ontology")) {
                        ont = new OwlOntology(elem);
                        String name = url.getFile();
                        int slashInx = name.lastIndexOf('/');
                        if (slashInx > -1) {
                            name = name.substring(slashInx + 1);
                        }
                        ont.setName(name);
                        ont.setUrl(url);
                        loading.put(url, ont);
                    } else if (tagName.equals("owlx:Annotation") || tagName.equals("owlx:VersionInfo")) {
                    } else if (tagName.equals("owl:Class") || tagName.equals("rdfs:Class") || tagName.equals("Class")) {
                        OwlClass.createFromElement(elem);
                    } else if (tagName.equals("owlx:SubClassOf")) {
                        parseSubClassElement(elem);
                    } else if (tagName.endsWith("ObjectProperty")) {
                        OwlObjectProperty.createFromElement(elem);
                    } else if (tagName.equals("owl:TransitiveProperty")) {
                        OwlObjectProperty prop = OwlObjectProperty.createFromElement(elem);
                        prop.setTransitive(true);
                    } else if (tagName.equals("owl:SymmetricProperty")) {
                        OwlObjectProperty prop = OwlObjectProperty.createFromElement(elem);
                        prop.setSymmetric(true);
                    } else if (tagName.equals("owl:FunctionalProperty")) {
                        OwlObjectProperty prop = OwlObjectProperty.createFromElement(elem);
                        prop.setFunctional(true);
                    } else if (tagName.equals("owl:InverseFunctionalProperty")) {
                        OwlObjectProperty prop = OwlObjectProperty.createFromElement(elem);
                        prop.setInverseFunctional(true);
                    } else if (tagName.endsWith("DatatypeProperty") || tagName.equals("rdf:Property")) {
                        OwlDatatypeProperty.createFromElement(elem);
                    } else if (tagName.endsWith("AnnotationProperty")) {
                        parseAnnotationProp(elem);
                    } else if (tagName.contains(":Description")) {
                    } else if (OwlClass.getClassNames().contains(tagName) || tagName.equals("owl:Thing") || tagName.endsWith(":Individual")) {
                        OwlThing.createFromElement(elem);
                    } else if (tagName.equals("owlx:SameIndividual")) {
                        parseSameThingElement(elem);
                    } else if (tagName.equals("owl:AllDifferent")) {
                    } else if (tagName.endsWith("DisjointClasses")) {
                        parseDisjointClasses(elem);
                    } else {
                        parseUnknownElement(elem);
                    }
                }
            }
            for (OwlThing thing : OwlThing.getAllThings()) {
                OwlClass cls = thing.getOwlClass();
                if (cls != null) {
                    cls.setDefinedValues(thing);
                    thing.validate();
                }
            }
        }
        return url;
    }

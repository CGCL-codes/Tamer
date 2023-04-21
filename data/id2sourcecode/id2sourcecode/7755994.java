    public void testSuperType() throws Exception {
        Map<String, String> clazMap = new HashMap<String, String>();
        clazMap.put("subtype1", SubType1.class.getName());
        clazMap.put("subtype2", SubType2.class.getName());
        Visitor visitor = new ClassToNamespaceVisitor(false);
        Strategy strategy = new VisitorStrategy(visitor);
        Serializer serializer = new Persister(strategy);
        MyMap map = new MyMap();
        SubType1 subtype1 = new SubType1();
        SubType2 subtype2 = new SubType2();
        StringWriter writer = new StringWriter();
        subtype1.text = "subtype1";
        subtype2.superType = subtype1;
        map.getInternalMap().put("one", subtype1);
        map.getInternalMap().put("two", subtype2);
        serializer.write(map, writer);
        serializer.write(map, System.out);
        serializer.read(MyMap.class, writer.toString());
    }

    @Test
    public void testSerializationVsJaxb() throws Exception {
        List<Times> times = new ArrayList<Times>();
        Times jaxb = new Times("jaxb");
        times.add(jaxb);
        Times serial = new Times("serialization");
        times.add(serial);
        Times xstreamt = new Times("xstreamXml");
        times.add(xstreamt);
        Times xstreamjson = new Times("xstreamJson");
        times.add(xstreamjson);
        long totalFlexOut = 0;
        long totalFlexIn = 0;
        Times jabsorbT = new Times("jabsorb");
        times.add(jabsorbT);
        int flexjsonsize = 0;
        Times jboss = new Times("jboss");
        times.add(jboss);
        Times hessian = new Times("hessian");
        times.add(hessian);
        {
            JAXBContext context = JAXBContext.newInstance(RootObject.class);
            Marshaller marshaller = context.createMarshaller();
            Unmarshaller unmarshaller = context.createUnmarshaller();
            for (int i = 0; i < 100; i++) {
                RootObject testOb = new RootObject();
                testOb.setId("" + i);
                testOb.setCreated(new Date());
                testOb.addOther(new SomeObject2("so-" + i));
                testOb.addOther(new SomeObject2("so2-" + i));
                StringWriter writer = new StringWriter(5000);
                long start = System.currentTimeMillis();
                marshaller.marshal(new JAXBElement(new QName(testOb.getClass().getSimpleName()), testOb.getClass(), testOb), writer);
                long duration = System.currentTimeMillis() - start;
                jaxb.addOut(duration);
                byte[] ba = writer.toString().getBytes();
                jaxb.setSize(ba.length);
                if (i == 0) System.out.println("jaxb=" + writer.toString());
                StringReader reader = new StringReader(writer.toString());
                start = System.currentTimeMillis();
                unmarshaller.unmarshal(new StreamSource(reader), testOb.getClass());
                duration = System.currentTimeMillis() - start;
                jaxb.addIn(duration);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutput out = new ObjectOutputStream(bos);
                start = System.currentTimeMillis();
                out.writeObject(testOb);
                out.close();
                duration = System.currentTimeMillis() - start;
                byte[] byteArray = bos.toByteArray();
                serial.addOut(duration);
                serial.setSize(byteArray.length);
                ByteArrayInputStream bin = new ByteArrayInputStream(byteArray);
                ObjectInputStream in = new ObjectInputStream(bin);
                start = System.currentTimeMillis();
                Object o = in.readObject();
                in.close();
                duration = System.currentTimeMillis() - start;
                serial.addIn(duration);
                String s;
            }
            for (Times time : times) {
                System.out.println(time);
            }
        }
    }

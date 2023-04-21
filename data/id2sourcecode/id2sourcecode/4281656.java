    public void testBean2XML() {
        try {
            Marshaller mar = context.createMarshaller();
            writer = new StringWriter();
            mar.marshal(bean, writer);
            out(writer);
            reader = new StringReader(writer.toString());
            Unmarshaller unmar = context.createUnmarshaller();
            bean = (AccountBean) unmar.unmarshal(reader);
            out(bean);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

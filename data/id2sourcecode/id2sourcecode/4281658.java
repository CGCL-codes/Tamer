    @Test
    public void testMap2XML() {
        AccountMapBean mapBean = new AccountMapBean();
        HashMap<String, AccountBean> map = new HashMap<String, AccountBean>();
        map.put("NO1", bean);
        bean = new AccountBean();
        bean.setAddress("ShangHai");
        bean.setEmail("magicshuai@126.com");
        bean.setId(2);
        bean.setName("magicshuai");
        Birthday day = new Birthday("2010-11-22");
        bean.setBirthday(day);
        map.put("NO2", bean);
        mapBean.setMap(map);
        try {
            context = JAXBContext.newInstance(AccountMapBean.class);
            Marshaller mar = context.createMarshaller();
            writer = new StringWriter();
            mar.marshal(mapBean, writer);
            out(writer);
            reader = new StringReader(writer.toString());
            Unmarshaller unmar = context.createUnmarshaller();
            mapBean = (AccountMapBean) unmar.unmarshal(reader);
            out(mapBean.getMap());
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

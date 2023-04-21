    public void testList2XML() {
        AccountListBean accountBeans = new AccountListBean();
        accountBeans.setName("google");
        List<Object> list = new ArrayList<Object>();
        list.add(bean);
        bean = new AccountBean();
        bean.setAddress("ShangHai");
        bean.setEmail("magicshuai@126.com");
        bean.setId(2);
        bean.setName("magicshuai");
        Birthday day = new Birthday("1987-11-22");
        bean.setBirthday(day);
        list.add(bean);
        accountBeans.setList(list);
        try {
            context = JAXBContext.newInstance(AccountListBean.class);
            Marshaller mar = context.createMarshaller();
            writer = new StringWriter();
            mar.marshal(accountBeans, writer);
            out(writer);
            reader = new StringReader(writer.toString());
            Unmarshaller unmar = context.createUnmarshaller();
            accountBeans = (AccountListBean) unmar.unmarshal(reader);
            out(accountBeans.getList().get(0));
            out(accountBeans.getList().get(1));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

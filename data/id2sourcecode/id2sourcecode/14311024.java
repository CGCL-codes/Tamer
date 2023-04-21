    public static void main(String[] args) throws IOException, MappingException, CastorException {
        URL url = new URL("http://keppardo.s42.eatj.com/bridge/dbbridge");
        URLConnection connection = url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        OutputStream out = connection.getOutputStream();
        Query qry = new Query();
        qry.setSql("select * from USERS where NICKNAME=?");
        DbParam param = new DbParam();
        param.setPos(1);
        param.setValue("pardo");
        qry.addParam(param);
        Marshaller marshaller = new Marshaller();
        Mapping mapping = new Mapping();
        mapping.loadMapping(new InputSource(Mapping.class.getResourceAsStream("/org/bridge/db/bean/xml/query.cm.xml")));
        marshaller.setMapping(mapping);
        marshaller.setWriter(new OutputStreamWriter(out));
        marshaller.marshal(qry);
        InputStream in = connection.getInputStream();
        byte[] response = IOUtils.toByteArray(in);
        System.out.println(new String(response));
    }

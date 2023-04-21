    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        URL url = new URL("http://co02intranet/sgdcent/cnt/vw_ss_ma_empleados_activos_to_xml.asp");
        InputStream inputStream = url.openStream();
        Document document = db.parse(inputStream);
        System.out.println(document.toString());
        inputStream.close();
    }

    public static void main(String[] args) {
        File in = new File("/home/dodo/Documents/sts/user-ait020/marctest/bilderatlasmw4_meta.mrc");
        InputStream input;
        try {
            input = new FileInputStream(in);
            MarcReader reader = new MarcStreamReader(input);
            DOMResult result = new DOMResult();
            if (true) {
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                MarcWriter writer = new MarcXmlWriter(bout, "UTF-8");
                while (reader.hasNext()) {
                    Record record = reader.next();
                    writer.write(record);
                }
                writer.close();
                Document doc = DOM.parse(bout.toByteArray());
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.transform(new DOMSource(doc), new StreamResult(System.out));
            }
            if (false) {
                MarcXmlWriter writer = new MarcXmlWriter(result);
                writer.setConverter(new AnselToUnicode());
                while (reader.hasNext()) {
                    Record record = (Record) reader.next();
                    writer.write(record);
                }
                writer.close();
            }
            input.close();
            Document doc = (Document) result.getNode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

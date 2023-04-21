    private IOObject loadData2IOObject(URL url) {
        ExampleTable table = null;
        ExampleSet es = null;
        SimpleAttributes attributes = new SimpleAttributes();
        Attribute idAtt = new PolynominalAttribute("Hash");
        attributes.addRegular(idAtt);
        attributes.setId(idAtt);
        attributes.addRegular(new NumericalAttribute("Špatně"));
        attributes.addRegular(new NumericalAttribute("Bez odpovědi"));
        attributes.addRegular(new NumericalAttribute("Správně"));
        attributes.addRegular(new NumericalAttribute("průměr"));
        Attribute labelAtt = new PolynominalAttribute("label");
        attributes.addRegular(labelAtt);
        List<Attribute> attibuteList = new ArrayList<Attribute>();
        attibuteList.add(attributes.getId());
        for (Attribute attribute : attributes) {
            attibuteList.add(attribute);
        }
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            in.readLine();
            Attribute[] attributeArray = new Attribute[5];
            attributeArray = attibuteList.toArray(attributeArray);
            DataRowFactory dataRowFactory = new DataRowFactory(DataRowFactory.TYPE_DOUBLE_ARRAY, '.');
            Iterator<SimpleArrayData> urlArrayDataIterator = new UrlArrayDataIterator(in).iterator();
            SimpleArrayDataRowReader dataRowReader = new SimpleArrayDataRowReader(dataRowFactory, attributeArray, urlArrayDataIterator);
            table = new MemoryExampleTable(attibuteList, dataRowReader);
            in.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<Attribute, String> specialAttributes = new HashMap<Attribute, String>();
        specialAttributes.put(attributes.getId(), "id");
        List<Attribute> regularAttributes = new ArrayList<Attribute>();
        for (Attribute attribute : attributes) {
            regularAttributes.add(attribute);
        }
        es = new SimpleExampleSet(table, regularAttributes, specialAttributes);
        return es;
    }

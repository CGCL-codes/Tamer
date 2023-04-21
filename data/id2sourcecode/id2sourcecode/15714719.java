    private ExampleSet loadData(URL url) {
        List<Attribute> attibuteList = new ArrayList<Attribute>();
        ExampleTable table = null;
        ExampleSet es = null;
        SimpleAttributes attributes = new SimpleAttributes();
        Attribute idAtt = new PolynominalAttribute("Hash");
        attributes.addRegular(idAtt);
        attributes.addRegular(new NumericalAttribute("NOK"));
        attributes.addRegular(new NumericalAttribute("NULL"));
        attributes.addRegular(new NumericalAttribute("OK"));
        attributes.addRegular(new NumericalAttribute("průměr"));
        attributes.setId(idAtt);
        attibuteList.add(attributes.getId());
        for (Attribute attribute : attributes) {
            attibuteList.add(attribute);
        }
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String line;
            line = in.readLine();
            Attribute[] attributeArray = new Attribute[4];
            attributeArray = attibuteList.toArray(attributeArray);
            DataRowFactory dataRowFactory = new DataRowFactory(DataRowFactory.TYPE_SHORT_ARRAY, '.');
            Iterator<SimpleArrayData> simpleArrayDataIterator = new MemorySimpleArrayIterator(in).iterator();
            SimpleArrayDataRowReader dataRowReader = new SimpleArrayDataRowReader(dataRowFactory, attributeArray, simpleArrayDataIterator);
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

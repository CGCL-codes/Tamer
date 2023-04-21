    @Override
    public synchronized Map<String, Object> getMetadata(ProgressMonitor mon) throws IOException, DASException, ParseException {
        if (metadata == null) {
            MyDASParser parser = new MyDASParser();
            URL url = new URL(adapter.getSource().toString() + ".das");
            parser.parse(url.openStream());
            das = parser.getDAS();
            if (variable == null) {
                variable = (String) das.getNames().nextElement();
                adapter.setVariable(variable);
            }
            metadata = getMetaData(variable);
        }
        return metadata;
    }

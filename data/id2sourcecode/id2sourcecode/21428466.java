    private List<CompletionContext> getVars(String file) throws DDSException, IOException, MalformedURLException, ParseException {
        List<CompletionContext> result = new ArrayList<CompletionContext>();
        int i = file.lastIndexOf('.');
        String sMyUrl = file.substring(0, i);
        URL url;
        url = new URL(sMyUrl + ".dds");
        MyDDSParser parser = new MyDDSParser();
        try {
            parser.parse(url.openStream());
        } catch (TokenMgrError ex) {
            throw new ParseException("Does not appear to be a DDS: " + url);
        } catch (RuntimeException ex) {
            throw new ParseException("Does not appear to be a DDS: " + url);
        }
        String[] vars = parser.getVariableNames();
        for (int j = 0; j < vars.length; j++) {
            result.add(new CompletionContext(CompletionContext.CONTEXT_PARAMETER_NAME, vars[j], this, "arg_0", null, null, true));
        }
        return result;
    }

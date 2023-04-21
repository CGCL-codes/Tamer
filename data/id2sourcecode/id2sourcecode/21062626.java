    @Override
    public void doParse(URL url) throws ParseException {
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(url.openStream());
        } catch (IOException ex) {
        }
        OCL2Parser parser = new OCL2Parser(getModel(), reader);
        try {
            parser.parse();
        } catch (IOException ex) {
        } catch (ParsingException e) {
            throw new ParseException(e.getMessage());
        } catch (LexException e) {
            throw new ParseException(e.getMessage());
        } catch (BuildingASTException e) {
            throw new ParseException(e.getMessage());
        } catch (SemanticException e) {
            throw new ParseException(e.getMessage());
        }
    }

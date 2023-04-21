    protected NamedList<List<NamedList>> analyzeValue(String value, AnalysisContext context) {
        Analyzer analyzer = context.getAnalyzer();
        if (!TokenizerChain.class.isInstance(analyzer)) {
            TokenStream tokenStream = null;
            try {
                tokenStream = analyzer.reusableTokenStream(context.getFieldName(), new StringReader(value));
                tokenStream.reset();
            } catch (IOException e) {
                throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, e);
            }
            NamedList<List<NamedList>> namedList = new NamedList<List<NamedList>>();
            namedList.add(tokenStream.getClass().getName(), convertTokensToNamedLists(analyzeTokenStream(tokenStream), context));
            return namedList;
        }
        TokenizerChain tokenizerChain = (TokenizerChain) analyzer;
        CharFilterFactory[] cfiltfacs = tokenizerChain.getCharFilterFactories();
        TokenizerFactory tfac = tokenizerChain.getTokenizerFactory();
        TokenFilterFactory[] filtfacs = tokenizerChain.getTokenFilterFactories();
        NamedList<List<NamedList>> namedList = new NamedList<List<NamedList>>();
        if (cfiltfacs != null) {
            String source = value;
            for (CharFilterFactory cfiltfac : cfiltfacs) {
                CharStream reader = CharReader.get(new StringReader(source));
                reader = cfiltfac.create(reader);
                source = writeCharStream(namedList, reader);
            }
        }
        TokenStream tokenStream = tfac.create(tokenizerChain.charStream(new StringReader(value)));
        List<Token> tokens = analyzeTokenStream(tokenStream);
        namedList.add(tokenStream.getClass().getName(), convertTokensToNamedLists(tokens, context));
        ListBasedTokenStream listBasedTokenStream = new ListBasedTokenStream(tokens);
        for (TokenFilterFactory tokenFilterFactory : filtfacs) {
            tokenStream = tokenFilterFactory.create(listBasedTokenStream);
            List<Token> tokenList = analyzeTokenStream(tokenStream);
            namedList.add(tokenStream.getClass().getName(), convertTokensToNamedLists(tokenList, context));
            listBasedTokenStream = new ListBasedTokenStream(tokenList);
        }
        return namedList;
    }

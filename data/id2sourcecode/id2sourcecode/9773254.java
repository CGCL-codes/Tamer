    public Grammar loadGrammar(String grammarReference, String mediaType, boolean loadReferences, boolean reloadGrammars, Vector loadedGrammars) throws GrammarException, IllegalArgumentException, IOException, EngineStateException, EngineException {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Load Grammar : {0} with media Type:{1}", new Object[] { grammarReference, mediaType });
            LOGGER.log(Level.FINE, "loadReferences : {0} reloadGrammars:{1}", new Object[] { loadReferences, reloadGrammars });
            LOGGER.log(Level.FINE, "there are {0} loaded grammars:", loadedGrammars.size());
        }
        ensureValidEngineState();
        if (recognizer != null) {
            final EngineMode mode = recognizer.getEngineMode();
            if (!mode.getSupportsMarkup()) {
                throw new EngineException("Engine doesn't support markup");
            }
        }
        URL url = new URL(grammarReference);
        InputStream grammarStream = url.openStream();
        SrgsRuleGrammarParser srgsParser = new SrgsRuleGrammarParser();
        Rule[] rules = srgsParser.load(grammarStream);
        if (rules != null) {
            BaseRuleGrammar brg = new BaseRuleGrammar(recognizer, grammarReference);
            brg.addRules(rules);
            brg.setAttributes(srgsParser.getAttributes());
            grammars.put(grammarReference, brg);
            return brg;
        }
        return null;
    }

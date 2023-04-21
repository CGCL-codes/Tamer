    private void parameterSingle(String key, String value, boolean commandLine, boolean reapply) throws PhramerException {
        if (key.equals("-x-level-good-probabilities")) {
            onlyNonPositiveLogProbabilitiesLevel = Integer.parseInt(value);
            return;
        }
        if (key.equals("-x-score-mask") || key.equals("-sc-mask")) {
            scoreMask = StringTools.tokenize(value, ",");
            return;
        }
        if (key.equals("-compatibility")) {
            compatibilityLevel = PhramerTools.getCompatibilityLevel(value);
            return;
        }
        if (key.equals("-x-future-cost-class")) {
            futureCostCalculatorClass = value;
            return;
        }
        if (key.equals("-ttable-file")) {
            _ttFile = value;
            return;
        }
        if (key.equals("-lmodel-file")) {
            _languageModelFiles = new String[1];
            _languageModelFiles[0] = value;
            return;
        }
        if (key.equals("-weight-x") || key.equals("-px")) {
            if (reapply && (weightX.length != 1)) throw new PhramerException("Reparse command line: different number of weight-x");
            weightX = new double[1];
            weightX[0] = Double.parseDouble(value);
            return;
        }
        if (key.equals("-weight-t") || key.equals("-tm")) {
            if (reapply && (weightT.length != 1)) throw new PhramerException("Reparse command line: different number of weight-t");
            weightT = new double[1];
            weightT[0] = Double.parseDouble(value);
            _weightT = true;
            return;
        }
        if (key.equals("-weight-l") || key.equals("-lm")) {
            if (reapply && (_lmWeights.length != 1)) throw new PhramerException("Reparse command line: different number of weight-l");
            _lmWeights = new double[1];
            _lmWeights[0] = Double.parseDouble(value);
            _weightL = true;
            return;
        }
        if (key.equals("-weight-d") || key.equals("-d")) {
            weightD = Double.parseDouble(value);
            _weightD = true;
            return;
        }
        if (key.equals("-weight-w") || key.equals("-w")) {
            weightW = Double.parseDouble(value);
            _weightW = true;
            return;
        }
        if (key.equals("-x-oov-probability") || key.equals("-x-oov")) {
            if (reapply && (_lmUnkLogProbability.length != 1)) throw new PhramerException("Reparse command line: different number of x-oov-probability");
            _lmUnkLogProbability = new double[1];
            _lmUnkLogProbability[0] = Double.parseDouble(value);
            return;
        }
        if (key.equals("-x-lm-lbound") || key.equals("-x-lb")) {
            if (reapply && (_lmMinLogProbability.length != 1)) throw new PhramerException("Reparse command line: different number of x-lm-lbound");
            _lmMinLogProbability = new double[1];
            _lmMinLogProbability[0] = Double.parseDouble(value);
            return;
        }
        if (key.equals("-x-context-length")) {
            lmContextLength = Integer.parseInt(value);
            return;
        }
        if (key.equals("-x-max-phrase-length")) {
            maxPhraseLength = Integer.parseInt(value);
            return;
        }
        if (key.equals("-ttable-limit")) {
            tTableLimit = Integer.parseInt(value);
            return;
        }
        if (key.equals("-ttable-threshold")) {
            tTableThreshold = Double.parseDouble(value);
            return;
        }
        if (key.equals("-stack") || key.equals("-s")) {
            stack = Integer.parseInt(value);
            return;
        }
        if (key.equals("-beam-threshold") || key.equals("-b")) {
            beamThreshold = Double.parseDouble(value);
            logBeamThreshold = MathTools.numberToLog(beamThreshold);
            return;
        }
        if (key.equals("-distortion-limit") || key.equals("-dl")) {
            distortionLimit = Integer.parseInt(value);
            return;
        }
        if (key.equals("-weight-marked")) {
            weightMarked = Double.parseDouble(value);
            return;
        }
        if (key.equals("-lmenc")) {
            encodingLM = value;
            return;
        }
        if (key.equals("-ttenc")) {
            encodingTT = value;
            return;
        }
        for (int i = 0; i < _class.length; i++) {
            if (key.equals(ClassInstantiation._NAMES[i])) {
                _class[i] = value;
                return;
            }
            if (key.equals(ClassInstantiation._NAMES[i] + "-param")) {
                _param[i] = new String[1];
                _param[i][0] = value;
                return;
            }
        }
        if (commandLine) {
            if (key.equals("-x-unk-words-transliterator")) {
                unkWordsTransliteratorClass = value;
                return;
            }
            if (key.equals("-inputtype")) return;
            if (key.equals("-config") || key.equals("-f")) return;
            if (key.equals("-server")) return;
            if (key.equals("-threads")) return;
            if (key.equals("-cache")) return;
            if (key.equals("-renc")) {
                encodingInput = value;
                return;
            }
            if (key.equals("-wenc")) {
                encodingOutput = value;
                return;
            }
            if (key.equals("-rescore") || key.equals("-r")) {
                ttStoreDetails = true;
                return;
            }
            if (key.equals("-verbose") || key.equals("-v")) {
                verboseLevel = Integer.parseInt(value);
                return;
            }
            if (key.equals("-lattice") || key.equals("-l")) {
                lattice = value;
                return;
            }
            if (key.equals("-read") || key.equals("-write") || key.equals("-start-id")) return;
        }
        throw new PhramerException("Invalid parameter: " + key);
    }

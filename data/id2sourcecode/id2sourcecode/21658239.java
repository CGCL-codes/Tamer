    @Override
    public void plot(final IAST args) {
        IAST funcs;
        ISymbol xVar;
        AST xArgs;
        if (args.size() < 3) {
            throw new IllegalArgumentException("At least two arguments needed.");
        }
        thisResolution = newResolution;
        final EvalEngine engine = EvalEngine.get();
        if (args.get(1).isList()) {
            funcs = (AST) args.get(1);
            numFuncs = funcs.size() - 1;
        } else {
            funcs = args;
            numFuncs = 1;
        }
        point = new double[numFuncs][thisResolution + 1];
        paintPoint = new int[thisResolution + 1];
        xPoint = new int[thisResolution + 1];
        color = new Color[numFuncs];
        xArgs = (AST) args.get(2);
        xVar = (ISymbol) xArgs.get(1);
        xText = xVar.toString();
        xMin = ((INum) engine.evaluate(N(xArgs.get(2)))).getRealPart();
        xMax = ((INum) engine.evaluate(N(xArgs.get(3)))).getRealPart();
        xRange = xMax - xMin;
        try {
            yMax = yMin = new UnaryNumerical(funcs.get(1), xVar, engine).value(xMin);
        } catch (final Exception e) {
            yMax = yMin = 0;
        }
        for (int func = 0; func < numFuncs; ++func) {
            populate(funcs, func, xVar, engine);
        }
        for (int counter = 3; counter < args.size(); ++counter) {
            final AST currentArgs = (AST) args.get(counter);
            if (getColor(currentArgs.get(1)) != null) {
                for (int i = 0; i < color.length; ++i) {
                    color[i] = getColor(currentArgs.get((i % (currentArgs.size() - 1)) + 1));
                }
            } else {
                yText = currentArgs.get(1).toString();
                yMin = ((INum) engine.evaluate(N(currentArgs.get(2)))).getRealPart();
                yMax = ((INum) engine.evaluate(N(currentArgs.get(3)))).getRealPart();
            }
        }
        if (yMax <= yMin) {
            if (yMax < 0) {
                yMax = 0;
            }
            if (yMin > 0) {
                yMin = 0;
            }
            if (yMax <= yMin) {
                yMax = yMin = (yMax + yMin) / 2;
                ++yMax;
                --yMin;
            }
        }
        yRange = yMax - yMin;
        setupText();
    }

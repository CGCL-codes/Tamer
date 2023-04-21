    private Term compute(Primitive what, ExtensibleMap<Variable, Variable> renamings, Set<Variable> globals) {
        What: switch(what) {
            case PLUS:
                computeArguments();
                try {
                    long sum = 0;
                    for (int i = 1; i < arity(); ++i) {
                        Term a = sub(i);
                        if (!Util.isConstant(a)) return null;
                        sum += Long.decode(a.constructor().symbol());
                    }
                    return rewrapWithProperties(factory.newConstruction(factory.makeConstructor(sum), GenericTerm.NO_BINDS, GenericTerm.NO_TERMS));
                } catch (NumberFormatException e) {
                }
                break;
            case MINUS:
                computeArguments();
                try {
                    if (!Util.isConstant(sub(1)) || !Util.isConstant(sub(2))) return null;
                    long left = Long.decode(sub(1).constructor().symbol());
                    long right = Long.decode(sub(2).constructor().symbol());
                    return rewrapWithProperties(factory.newConstruction(factory.makeConstructor(left - right), GenericTerm.NO_BINDS, GenericTerm.NO_TERMS));
                } catch (NumberFormatException e) {
                }
                break;
            case TIMES:
                computeArguments();
                try {
                    long product = 1;
                    for (int i = 1; i < arity(); ++i) {
                        Term a = sub(i);
                        if (!Util.isConstant(a)) return null;
                        product *= Long.decode(a.toString());
                    }
                    return rewrapWithProperties(factory.newConstruction(factory.makeConstructor(product), GenericTerm.NO_BINDS, GenericTerm.NO_TERMS));
                } catch (NumberFormatException e) {
                }
                break;
            case DIVIDE:
                computeArguments();
                if (!Util.isConstant(sub(1)) || !Util.isConstant(sub(2))) break;
                try {
                    long left = Long.decode(sub(1).constructor().symbol());
                    long right = Long.decode(sub(2).constructor().symbol());
                    return rewrapWithProperties(factory.newConstruction(factory.makeConstructor(left / right), GenericTerm.NO_BINDS, GenericTerm.NO_TERMS));
                } catch (NumberFormatException e) {
                }
                break;
            case MODULO:
                computeArguments();
                if (!Util.isConstant(sub(1)) || !Util.isConstant(sub(2))) break;
                try {
                    long left = Long.decode(sub(1).constructor().symbol());
                    long right = Long.decode(sub(2).constructor().symbol());
                    return rewrapWithProperties(factory.newConstruction(factory.makeConstructor(left % right), GenericTerm.NO_BINDS, GenericTerm.NO_TERMS));
                } catch (NumberFormatException e) {
                }
                break;
            case HEX:
                computeArguments();
                if (!Util.isConstant(sub(1))) break;
                try {
                    return rewrapWithProperties(factory.newConstruction(factory.makeConstructor(Long.parseLong(sub(1).constructor().symbol(), 16)), GenericTerm.NO_BINDS, GenericTerm.NO_TERMS));
                } catch (NumberFormatException e) {
                }
                break;
            case BIT_AND:
                computeArguments();
                if (!Util.isConstant(sub(1)) || !Util.isConstant(sub(2))) break;
                try {
                    long left = Long.decode(sub(1).constructor().symbol());
                    long right = Long.decode(sub(2).constructor().symbol());
                    return rewrapWithProperties(factory.newConstruction(factory.makeConstructor(left & right), GenericTerm.NO_BINDS, GenericTerm.NO_TERMS));
                } catch (NumberFormatException e) {
                }
                break;
            case BIT_OR:
                computeArguments();
                if (!Util.isConstant(sub(1)) || !Util.isConstant(sub(2))) break;
                try {
                    long left = Long.decode(sub(1).constructor().symbol());
                    long right = Long.decode(sub(2).constructor().symbol());
                    return rewrapWithProperties(factory.newConstruction(factory.makeConstructor(left | right), GenericTerm.NO_BINDS, GenericTerm.NO_TERMS));
                } catch (NumberFormatException e) {
                }
                break;
            case BIT_XOR:
                computeArguments();
                if (!Util.isConstant(sub(1)) || !Util.isConstant(sub(2))) break;
                try {
                    long left = Long.decode(sub(1).constructor().symbol());
                    long right = Long.decode(sub(2).constructor().symbol());
                    return rewrapWithProperties(factory.newConstruction(factory.makeConstructor(left ^ right), GenericTerm.NO_BINDS, GenericTerm.NO_TERMS));
                } catch (NumberFormatException e) {
                }
                break;
            case BIT_NOT:
                computeArguments();
                if (!Util.isConstant(sub(1))) break;
                try {
                    long bits = Long.decode(sub(1).constructor().symbol());
                    return rewrapWithProperties(factory.newConstruction(factory.makeConstructor(~bits), GenericTerm.NO_BINDS, GenericTerm.NO_TERMS));
                } catch (NumberFormatException e) {
                }
                break;
            case BIT_MINUS:
                computeArguments();
                if (!Util.isConstant(sub(1)) || !Util.isConstant(sub(2))) break;
                try {
                    long left = Long.decode(sub(1).constructor().symbol());
                    long right = Long.decode(sub(2).constructor().symbol());
                    return rewrapWithProperties(factory.newConstruction(factory.makeConstructor(left & ~right), GenericTerm.NO_BINDS, GenericTerm.NO_TERMS));
                } catch (NumberFormatException e) {
                }
                break;
            case BIT_SUB_SET_EQ:
                {
                    computeArguments();
                    if (!Util.isConstant(sub(1)) || !Util.isConstant(sub(2))) break;
                    try {
                        long left = Long.decode(sub(1).constructor().symbol());
                        long right = Long.decode(sub(2).constructor().symbol());
                        return rewrapWithProperties(factory.newConstruction(((left & (~right)) == 0 ? factory.trueConstructor : factory.nilConstructor), GenericTerm.NO_BINDS, GenericTerm.NO_TERMS));
                    } catch (NumberFormatException e) {
                    }
                    break;
                }
            case EQ:
                {
                    computeArguments();
                    if (!Util.isConstant(sub(1)) || !Util.isConstant(sub(2))) break;
                    return rewrapWithProperties(factory.newConstruction((sub(1).equals(sub(2)) ? factory.trueConstructor : factory.nilConstructor), GenericTerm.NO_BINDS, GenericTerm.NO_TERMS));
                }
            case NE:
                {
                    computeArguments();
                    if (!Util.isConstant(sub(1)) || !Util.isConstant(sub(2))) break;
                    return rewrapWithProperties(factory.newConstruction((sub(1).equals(sub(2)) ? factory.nilConstructor : factory.trueConstructor), GenericTerm.NO_BINDS, GenericTerm.NO_TERMS));
                }
            case LE:
                {
                    computeArguments();
                    if (!Util.isConstant(sub(1)) || !Util.isConstant(sub(2))) break;
                    int left = Integer.decode(sub(1).constructor().symbol());
                    int right = Integer.decode(sub(2).constructor().symbol());
                    return rewrapWithProperties(factory.newConstruction((left <= right ? factory.trueConstructor : factory.nilConstructor), GenericTerm.NO_BINDS, GenericTerm.NO_TERMS));
                }
            case GE:
                {
                    computeArguments();
                    if (!Util.isConstant(sub(1)) || !Util.isConstant(sub(2))) break;
                    int left = Integer.decode(sub(1).constructor().symbol());
                    int right = Integer.decode(sub(2).constructor().symbol());
                    return rewrapWithProperties(factory.newConstruction((left >= right ? factory.trueConstructor : factory.nilConstructor), GenericTerm.NO_BINDS, GenericTerm.NO_TERMS));
                }
            case LT:
                {
                    computeArguments();
                    if (!Util.isConstant(sub(1)) || !Util.isConstant(sub(2))) break;
                    int left = Integer.decode(sub(1).constructor().symbol());
                    int right = Integer.decode(sub(2).constructor().symbol());
                    return rewrapWithProperties(factory.newConstruction((left < right ? factory.trueConstructor : factory.nilConstructor), GenericTerm.NO_BINDS, GenericTerm.NO_TERMS));
                }
            case GT:
                {
                    computeArguments();
                    if (!Util.isConstant(sub(1)) || !Util.isConstant(sub(2))) break;
                    int left = Integer.decode(sub(1).constructor().symbol());
                    int right = Integer.decode(sub(2).constructor().symbol());
                    return rewrapWithProperties(factory.newConstruction((left > right ? factory.trueConstructor : factory.nilConstructor), GenericTerm.NO_BINDS, GenericTerm.NO_TERMS));
                }
            case CONCATENATION:
                {
                    computeArguments();
                    StringBuilder b = new StringBuilder();
                    for (int i = 1; i < arity(); ++i) {
                        Term a = sub(i);
                        if (!Util.isConstant(a)) return null;
                        String text;
                        switch(a.kind()) {
                            case CONSTRUCTION:
                                text = a.constructor().symbol();
                                break;
                            case META_APPLICATION:
                                text = a.metaVariable();
                                break;
                            case VARIABLE_USE:
                                text = a.variable().name();
                                break;
                            default:
                                break What;
                        }
                        b.append(text);
                    }
                    return rewrapWithProperties(factory.newConstruction(factory.makeConstructor(b.toString()), GenericTerm.NO_BINDS, GenericTerm.NO_TERMS));
                }
            case INDEX:
                {
                    computeArguments();
                    if (!Util.isConstant(sub(1)) || !Util.isConstant(sub(2))) break;
                    int index = sub(1).constructor().symbol().indexOf(sub(2).constructor().symbol());
                    return rewrapWithProperties(factory.newConstruction(factory.makeConstructor(index), GenericTerm.NO_BINDS, GenericTerm.NO_TERMS));
                }
            case CONTAINS:
                {
                    computeArguments();
                    if (!Util.isConstant(sub(1)) || !Util.isConstant(sub(2))) break;
                    boolean contains = sub(1).constructor().symbol().contains(sub(2).constructor().symbol());
                    return rewrapWithProperties(factory.newConstruction((contains ? factory.trueConstructor : factory.nilConstructor), GenericTerm.NO_BINDS, GenericTerm.NO_TERMS));
                }
            case STARTS_WITH:
                {
                    computeArguments();
                    if (!Util.isConstant(sub(1)) || !Util.isConstant(sub(2))) break;
                    boolean startsWith = sub(1).constructor().symbol().startsWith(sub(2).constructor().symbol());
                    return rewrapWithProperties(factory.newConstruction((startsWith ? factory.trueConstructor : factory.nilConstructor), GenericTerm.NO_BINDS, GenericTerm.NO_TERMS));
                }
            case ENDS_WITH:
                {
                    computeArguments();
                    if (!Util.isConstant(sub(1)) || !Util.isConstant(sub(2))) break;
                    boolean endsWith = sub(1).constructor().symbol().endsWith(sub(2).constructor().symbol());
                    return rewrapWithProperties(factory.newConstruction((endsWith ? factory.trueConstructor : factory.nilConstructor), GenericTerm.NO_BINDS, GenericTerm.NO_TERMS));
                }
            case LENGTH:
                {
                    computeArguments();
                    if (!Util.isConstant(sub(1))) break;
                    int length = sub(1).constructor().symbol().length();
                    return rewrapWithProperties(factory.newConstruction(factory.makeConstructor(length), GenericTerm.NO_BINDS, GenericTerm.NO_TERMS));
                }
            case REPLACE:
                {
                    computeArguments();
                    if (!Util.isConstant(sub(1)) || !Util.isConstant(sub(2)) || !Util.isConstant(sub(3))) break;
                    String symbol = sub(1).constructor().symbol().replace(sub(2).constructor().symbol(), sub(3).constructor().symbol());
                    return rewrapWithProperties(factory.newConstruction(factory.makeConstructor(symbol), GenericTerm.NO_BINDS, GenericTerm.NO_TERMS));
                }
            case SQUASH:
                {
                    computeArguments();
                    if (!Util.isConstant(sub(1))) break;
                    String symbol = Util.symbol(sub(1)).replaceAll("[\\n\\t ]+", " ").trim();
                    return rewrapWithProperties(factory.newConstruction(factory.makeConstructor(symbol), GenericTerm.NO_BINDS, GenericTerm.NO_TERMS));
                }
            case SUBSTRING:
                {
                    computeArguments();
                    if (!Util.isConstant(sub(1)) || !Util.isConstant(sub(2)) || (arity() == 4 && !Util.isConstant(sub(3)))) break;
                    String symbol = (arity() == 4 ? sub(1).constructor().symbol().substring(Integer.parseInt(sub(2).constructor().symbol()), Integer.parseInt(sub(3).constructor().symbol())) : sub(1).constructor().symbol().substring(Integer.parseInt(sub(2).constructor().symbol())));
                    return rewrapWithProperties(factory.newConstruction(factory.makeConstructor(symbol), GenericTerm.NO_BINDS, GenericTerm.NO_TERMS));
                }
            case ESCAPE:
                {
                    computeArguments();
                    if (!Util.isConstant(sub(1))) break;
                    String string = quote(sub(1).constructor().symbol());
                    return rewrapWithProperties(factory.newConstruction(factory.makeConstructor(string), GenericTerm.NO_BINDS, GenericTerm.NO_TERMS));
                }
            case MANGLE:
                {
                    computeArguments();
                    if (!Util.isConstant(sub(1))) break;
                    String string = Util.quoteJavaIdentifierPart(Util.symbol(sub(1)));
                    return rewrapWithProperties(factory.newConstruction(factory.makeConstructor(string), GenericTerm.NO_BINDS, GenericTerm.NO_TERMS));
                }
            case SPLIT:
                {
                    computeArguments();
                    if (!Util.isConstant(sub(1)) || !Util.isConstant(sub(2)) || arity() != 3) break;
                    String string = sub(1).constructor().symbol();
                    String regex = sub(2).constructor().symbol();
                    String[] split = string.split(regex);
                    Term sequence = factory.nil();
                    final Variable[] nobinds = {};
                    for (int i = split.length - 1; i >= 0; --i) sequence = factory.cons(nobinds, factory.constant(split[i]), nobinds, sequence);
                    return rewrapWithProperties(sequence);
                }
            case UPTO_FIRST:
                {
                    computeArguments();
                    if (!Util.isConstant(sub(1)) || !Util.isConstant(sub(2)) || (arity() > 3 && !Util.isConstant(sub(3)))) break;
                    String string = sub(1).constructor().symbol();
                    String first = sub(2).constructor().symbol();
                    int index = string.indexOf(first);
                    return rewrapWithProperties(index < 0 ? (arity() > 3 ? sub(3) : sub(1)) : factory.constant(string.substring(0, index + first.length())));
                }
            case BEFORE_FIRST:
                {
                    computeArguments();
                    if (!Util.isConstant(sub(1)) || !Util.isConstant(sub(2)) || (arity() > 3 && !Util.isConstant(sub(3)))) break;
                    String string = sub(1).constructor().symbol();
                    String first = sub(2).constructor().symbol();
                    int index = string.indexOf(first);
                    return rewrapWithProperties(index < 0 ? (arity() > 3 ? sub(3) : sub(1)) : factory.constant(string.substring(0, index)));
                }
            case AFTER_FIRST:
                {
                    computeArguments();
                    if (!Util.isConstant(sub(1)) || !Util.isConstant(sub(2)) || (arity() > 3 && !Util.isConstant(sub(3)))) break;
                    String string = sub(1).constructor().symbol();
                    String first = sub(2).constructor().symbol();
                    int index = string.indexOf(first);
                    return rewrapWithProperties(index < 0 ? (arity() > 3 ? sub(3) : factory.constant("")) : factory.constant(string.substring(index + first.length())));
                }
            case EMPTY_SEQUENCE:
                {
                    computeArguments();
                    if (sub(1).kind() == Kind.CONSTRUCTION) {
                        return rewrapWithProperties(Util.isNull(sub(1)) ? factory.constant("True") : factory.nil());
                    }
                    break;
                }
            case CONSTRUCTION:
                {
                    computeArguments();
                    if (arity() == 1 || !Util.isConstant(sub(1))) break;
                    return rewrapWithProperties(unquoteConstructionAndArguments(factory, sub(1).constructor(), (arity() <= 2 ? factory.nil() : sub(2)), renamings));
                }
            case VARIABLE:
                {
                    computeArguments();
                    if (arity() == 1 || !Util.isConstant(sub(1))) break;
                    return rewrapWithProperties(factory.newVariableUse(factory.makeVariable(sub(1).constructor().symbol(), true)));
                }
            case META_APPLICATION:
                {
                    computeArguments();
                    if (arity() == 1 || !Util.isConstant(sub(1)) || arity() == 2 || !SequenceIterator.isSequencing(sub(2))) break;
                    return rewrapWithProperties(factory.newMetaApplication(sub(1).constructor().symbol(), (arity() <= 2 ? NO_TERMS : Util.unconsArray(sub(2), false, renamings))));
                }
            case PROPERTY_NAMED:
                {
                    computeArguments();
                    if (!Util.isConstant(sub(1))) break;
                    String name = sub(1).constructor().symbol();
                    Term value = sub(2);
                    Term owner = sub(3);
                    return rewrapWithProperties(GenericTerm.wrapWithProperty(name, value, owner, false));
                }
            case PROPERTY_NAMED_NOT:
                {
                    computeArguments();
                    if (!Util.isConstant(sub(1))) break;
                    String name = sub(1).constructor().symbol();
                    Term owner = sub(2);
                    return rewrapWithProperties(GenericTerm.wrapWithNotProperty(name, (GenericTerm) owner, false));
                }
            case PROPERTY_VARIABLE:
                {
                    computeArguments();
                    Variable property;
                    if (sub(1).kind() == Kind.VARIABLE_USE) property = sub(1).variable(); else if (Util.isConstant(sub(1))) property = factory.makeVariable(sub(1).constructor().symbol(), true); else break;
                    Term value = sub(2);
                    Term owner = sub(3);
                    return rewrapWithProperties(GenericTerm.wrapWithProperty(property, value, owner, false));
                }
            case PROPERTY_VARIABLE_NOT:
                {
                    computeArguments();
                    Variable property;
                    if (sub(1).kind() == Kind.VARIABLE_USE) property = sub(1).variable(); else if (Util.isConstant(sub(1))) property = factory.makeVariable(sub(1).constructor().symbol(), true); else break;
                    Term owner = sub(2);
                    try {
                        return rewrapWithProperties(GenericTerm.wrapWithNotProperty(property, (GenericTerm) owner, false));
                    } catch (CRSException e) {
                        break;
                    }
                }
            case PROPERTY_COLLECT:
                {
                    computeArguments();
                    String metavar;
                    if (sub(1).kind() == Kind.META_APPLICATION && sub(1).arity() == 0) metavar = sub(1).metaVariable(); else if (Util.isConstant(sub(1))) metavar = sub(1).constructor().symbol(); else break;
                    Term owner = sub(2);
                    return rewrapWithProperties(GenericTerm.wrapWithPropertiesRef(metavar, (GenericTerm) owner, false));
                }
            case PROPERTIES:
                {
                    computeArguments();
                    return rewrapWithProperties(unquoteProperties(factory, sub(1), sub(2)));
                }
            case MATCH:
                break;
            case PARSE:
            case PARSE_URL:
            case PARSE_TEXT:
            case PARSE_RESOURCE:
            case LOAD:
            case LOAD_TERM:
                {
                    computeArguments();
                    try {
                        for (int i = 1; i < arity(); ++i) if (!Util.isConstant(sub(i))) break What;
                        String category;
                        String resource;
                        Reader reader;
                        switch(what) {
                            case PARSE:
                            case PARSE_URL:
                                {
                                    URL base = new URL("file:.");
                                    Term parseContextURL = Util.materialize(factory.get(Factory.BASE_URL), false);
                                    if (parseContextURL != null) base = new URL(base, parseContextURL.toString());
                                    if (arity() == 2) {
                                        category = null;
                                        URL url = new URL(base, Util.symbol(sub(1)));
                                        resource = url.toExternalForm();
                                        reader = new InputStreamReader(url.openStream());
                                    } else {
                                        category = Util.isNull(sub(1)) ? null : sub(1).constructor().symbol();
                                        URL url = new URL(base, Util.symbol(sub(2)));
                                        resource = url.toExternalForm();
                                        reader = new InputStreamReader(url.openStream());
                                    }
                                    break;
                                }
                            case PARSE_TEXT:
                                {
                                    category = Util.symbol(sub(1));
                                    resource = null;
                                    reader = new StringReader(Util.symbol(sub(2)));
                                    break;
                                }
                            case PARSE_RESOURCE:
                                {
                                    category = Util.symbol(sub(1));
                                    resource = Util.symbol(sub(2));
                                    reader = Util.resourceReader(factory, resource);
                                    break;
                                }
                            case LOAD:
                                {
                                    resource = Util.symbol(sub(1));
                                    reader = Util.resourceReader(factory, resource);
                                    category = (arity() <= 2 || !Util.isTrue(sub(2)) ? null : Util.symbol(sub(2)));
                                    break;
                                }
                            case LOAD_TERM:
                                {
                                    resource = Util.quoteJavaIdentifierPart(Util.symbol(sub(1)));
                                    reader = Util.resourceReader(factory, resource);
                                    category = null;
                                    break;
                                }
                            default:
                                break What;
                        }
                        if (Util.getInteger(factory, Factory.VERBOSE_OPTION, 0) > 0) factory.message(toString(), false);
                        Buffer buffer = new Buffer(factory);
                        Sink bufferSink = buffer.sink();
                        Map<String, Variable> free = null;
                        if (globals != null) {
                            free = new HashMap<String, Variable>();
                            for (Variable v : globals) free.put(v.name(), v);
                        }
                        factory.parser(factory).parse(bufferSink, category, reader, resource, 1, 1, null, free);
                        Term result = buffer.term(true);
                        assert result != null : "Parser did not build complete term.";
                        return rewrapWithProperties(result);
                    } catch (CRSException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            case SAVE_TERM:
                computeArgument(1);
                if (Util.isConstant(sub(1))) {
                    String resource = Util.quoteJavaIdentifierPart(Util.symbol(sub(1)));
                    try {
                        Appendable w = new WriterAppender(new FileWriter(resource));
                        sub(2).appendTo(w, new HashMap<Variable, String>(), Integer.MAX_VALUE, true);
                    } catch (IOException e) {
                        throw new RuntimeException(toString() + " failed", e);
                    }
                    return sub(3);
                }
                break;
            case SCRIPT:
                computeArgument(2);
                if (arity() == 2 || Util.isConstant(sub(2))) {
                    try {
                        SequenceBuffer result = new SequenceBuffer(factory);
                        Builder builder = factory.builder();
                        builder.load(result.sink(), sub(1));
                        if (arity() > 2) {
                            factory.set(sub(2).constructor().symbol(), (GenericCRS) builder.toCRS(true));
                        }
                        return rewrapWithProperties(result.term(true));
                    } catch (CRSException e) {
                        throw new RuntimeException(toString() + " failed", e);
                    }
                }
                break;
            case NORMALIZE:
                computeArguments();
                if (Util.isConstant(sub(2))) {
                    Constructor c = factory.get(sub(2).constructor().symbol()).constructor();
                    if (c != null && c instanceof CRS) {
                        try {
                            return rewrapWithProperties(((CRS) c).normalize(sub(1)));
                        } catch (CRSException e) {
                        }
                    }
                }
                break;
            case URL:
                computeArguments();
                computeArguments();
                if (Util.isConstant(sub(1)) && (arity() == 2 || Util.isConstant(sub(2)))) {
                    String name = sub(1).constructor().symbol();
                    try {
                        URL base = new URL("file:.");
                        Term parseContextURL = Util.materialize(((GenericFactory) factory).get(Factory.BASE_URL), false);
                        if (parseContextURL != null) base = new URL(base, parseContextURL.toString());
                        if (arity() > 2) base = new URL(base, sub(2).constructor().symbol());
                        URL url = new URL(base, name);
                        return rewrapWithProperties(factory.newConstruction(factory.makeConstructor(url), GenericTerm.NO_BINDS, GenericTerm.NO_TERMS));
                    } catch (MalformedURLException e) {
                    }
                }
                break;
            case IF:
                {
                    Term test = compute(sub(1), null);
                    if (test == null) test = sub(1);
                    if (Util.isConstant(test)) {
                        Constructor c = test.constructor();
                        if (!Util.isNull(c)) {
                            Term t = compute(sub(2), null);
                            return rewrapWithProperties(t != null ? t : sub(2));
                        } else {
                            if (arity() == 3) return rewrapWithProperties(factory.nil()); else {
                                Term t = compute(sub(3), null);
                                return rewrapWithProperties(t != null ? t : sub(3));
                            }
                        }
                    }
                    break;
                }
            case PICK:
                {
                    Term pick = compute(sub(1), null);
                    if (pick == null) pick = sub(1);
                    if (Util.isConstant(pick)) {
                        try {
                            int index = Integer.decode(Util.symbol(pick));
                            Term list = sub(2);
                            if (index < 0) break What;
                            while (index-- > 0) {
                                if (!Util.isCons(list.constructor())) break What;
                                list = list.sub(1);
                            }
                            if (!Util.isCons(list.constructor())) break What;
                            return rewrapWithProperties(sub(0));
                        } catch (NumberFormatException e) {
                        }
                    }
                    break;
                }
            case IF_ZERO:
                {
                    Term test = compute(sub(1), null);
                    if (test == null) test = sub(1);
                    if (Util.isConstant(test)) {
                        Constructor c = test.constructor();
                        if ("0".equals(c.symbol())) {
                            Term t = compute(sub(2), null);
                            return rewrapWithProperties(t != null ? t : sub(2));
                        } else {
                            if (arity() == 3) return rewrapWithProperties(factory.nil()); else {
                                Term t = compute(sub(3), null);
                                return rewrapWithProperties(t != null ? t : sub(3));
                            }
                        }
                    }
                    break;
                }
            case IF_EMPTY:
                {
                    Term test = compute(sub(1), null);
                    if (test == null) test = sub(1);
                    if (Util.isConstant(test)) {
                        Constructor c = test.constructor();
                        if ("".equals(c.symbol()) || Util.isNull(c)) {
                            Term t = compute(sub(2), null);
                            return rewrapWithProperties(t != null ? t : sub(2));
                        } else {
                            if (arity() == 3) return rewrapWithProperties(factory.nil()); else {
                                Term t = compute(sub(3), null);
                                return rewrapWithProperties(t != null ? t : sub(3));
                            }
                        }
                    }
                    break;
                }
            case IF_DEF:
                {
                    Term key = compute(sub(1), null);
                    if (key == null) key = sub(1);
                    boolean defined = false;
                    if (sub(0) instanceof PropertiesConstraintsWrapper) {
                        PropertiesConstraintsWrapper propertiesWrapper = (PropertiesConstraintsWrapper) sub(0);
                        WhichKey: switch(key.kind()) {
                            case CONSTRUCTION:
                                {
                                    String symbol = key.constructor().symbol();
                                    Map<String, Term> properties = propertiesWrapper.getLocalProperties();
                                    if (properties.containsKey(symbol)) {
                                        defined = properties.get(symbol) != null;
                                    } else {
                                        if (propertiesWrapper.hasUnknownKeys()) break What;
                                    }
                                    break;
                                }
                            case VARIABLE_USE:
                                {
                                    Variable variable = key.variable();
                                    Map<Variable, Term> properties = propertiesWrapper.getLocalVariableProperties();
                                    if (properties.containsKey(variable)) {
                                        defined = properties.get(variable) != null;
                                    } else {
                                        if (propertiesWrapper.hasUnknownKeys()) break What;
                                    }
                                    break;
                                }
                            case META_APPLICATION:
                                {
                                    String metaVariable = key.metaVariable();
                                    for (Pair<String, Term> p : propertiesWrapper.getLocalMetaProperties()) {
                                        if (metaVariable.equals(p.getKey())) {
                                            defined = p.getValue() != null;
                                            break WhichKey;
                                        }
                                    }
                                }
                            default:
                                break What;
                        }
                    }
                    if (!defined) {
                        PropertiesHolder properties = (PropertiesHolder) sub(0).constructor();
                        switch(key.kind()) {
                            case CONSTRUCTION:
                                defined = properties.getProperty(key.constructor().symbol()) != null;
                                break;
                            case VARIABLE_USE:
                                defined = properties.getProperty(key.variable()) != null;
                                break;
                            default:
                                break What;
                        }
                    }
                    if (!defined && Util.isConstant(sub(1))) {
                        defined = ((GenericFactory) factory).defined(sub(1).constructor().symbol());
                    }
                    if (defined) {
                        Term t = compute(sub(2), null);
                        return rewrapWithProperties(t != null ? t : sub(2));
                    } else {
                        if (arity() == 3) return factory.nil(); else {
                            Term t = compute(sub(3), null);
                            return rewrapWithProperties(t != null ? t : sub(3));
                        }
                    }
                }
            case IF_LINEAR:
                {
                    Term use = compute(sub(1), null);
                    if (use == null) use = sub(1);
                    if (use.kind() != Kind.VARIABLE_USE) break What;
                    return (!use.variable().promiscuous() ? sub(2) : arity() == 3 ? factory.nil() : sub(3));
                }
            case GET:
                {
                    Term key = compute(sub(1), null);
                    if (key == null) key = sub(1);
                    if (key.kind() == Kind.VARIABLE_USE || Util.isConstant(key)) {
                        Term value = null;
                        PropertiesHolder properties = (sub(0) instanceof PropertiesConstraintsWrapper ? (PropertiesHolder) sub(0) : sub(0).constructor());
                        switch(key.kind()) {
                            case CONSTRUCTION:
                                value = properties.getProperty(key.constructor().symbol());
                                break;
                            case VARIABLE_USE:
                                value = properties.getProperty(key.variable());
                                break;
                        }
                        if (value == null && Util.isConstant(key)) {
                            Stub lookup = ((GenericFactory) factory).get(key.constructor().symbol());
                            if (lookup != null) value = lookup.copy(false, null);
                        }
                        if (value != null) return rewrapWithProperties(value);
                        if (arity() > 2) {
                            value = compute(sub(2), null);
                            return rewrapWithProperties(value != null ? value : sub(2));
                        }
                    }
                    break;
                }
            case SORT_OF:
                {
                    Term term = sub(1);
                    if (term.kind() == Kind.CONSTRUCTION) {
                        Set<String> sorts = factory.sortsOf(term.constructor());
                        Term t = factory.nil();
                        if (sorts != null) for (String sort : sorts) t = factory.cons(NO_BIND, factory.constant(sort), NO_BIND, t);
                        return t;
                    }
                    return factory.nil();
                }
            case CHECK_SORT_OF:
                {
                    computeArguments();
                    if (!Util.isConstant(sub(1))) break;
                    String sort = Util.symbol(sub(1));
                    Term term = sub(2);
                    if (term.kind() == Kind.CONSTRUCTION && factory.sortsOf(term.constructor()).contains(sort)) {
                        try {
                            factory.checkSorts("Sort error: ", term, new HashMap<String, Pair<String, Term[]>>(), new HashMap<Variable, String>(), true, false);
                            return factory.truth();
                        } catch (CRSException e) {
                            System.err.println("Sort error: " + e.getMessage());
                        }
                    }
                    return factory.nil();
                }
            case PRINT:
                computeArguments();
                try {
                    sub(1).appendTo(System.out, new HashMap<Variable, String>(), Integer.MAX_VALUE, true);
                    System.out.println();
                    System.out.flush();
                    return rewrapWithProperties(arity() == 2 ? sub(1) : sub(2));
                } catch (IOException e) {
                }
                break;
            case FORMAT_NUMBER:
                {
                    computeArguments();
                    if (!Util.isConstant(sub(1))) break;
                    if (arity() == 2) return rewrapWithProperties(factory.constant(Util.symbol(sub(1)))); else break;
                }
            case SHOW:
                return rewrapWithProperties(factory.constant(sub(1).toString()));
            case COMPUTE:
                return rewrapWithProperties(sub(1));
            case ECHO:
                computeArguments();
                System.out.print(sub(1).constructor().symbol());
                System.out.flush();
                return rewrapWithProperties(arity() == 2 ? factory.nil() : sub(2));
            case DUMP:
                try {
                    Map<Variable, String> used = new HashMap<Variable, String>();
                    sub(1).appendTo(System.out, used, Integer.MAX_VALUE, true);
                    System.out.print(" ");
                    sub(2).appendTo(System.out, used, Integer.MAX_VALUE, true);
                    System.out.println();
                    System.out.flush();
                    return rewrapWithProperties(sub(2));
                } catch (IOException e) {
                }
                break;
            case TRACE:
                try {
                    sub(1).appendTo(System.err, new HashMap<Variable, String>(), Integer.MAX_VALUE, true);
                    System.err.println();
                    System.err.flush();
                    return rewrapWithProperties(arity() == 2 ? sub(1) : sub(2));
                } catch (IOException e) {
                }
                break;
            case ERROR:
                computeArguments();
                for (int i = 0; i < arity(); ++i) if (!Util.isConstant(sub(i))) break What;
                error();
                break;
            case IGNORE:
                break;
        }
        return null;
    }

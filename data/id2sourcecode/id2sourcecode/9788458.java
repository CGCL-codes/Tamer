    public ConstantString run(Session session, MaverickString[] args) throws MaverickException {
        this.session = session;
        Vector files = new Vector();
        boolean parseOnly = false;
        Factory factory = session.getFactory();
        MaverickString status = session.getStatus();
        Properties orig = session.getProperties();
        Properties properties = new Properties(orig);
        int index = 0;
        while (index < args.length) {
            MaverickString arg = args[index++];
            switch(arg.charAt(0)) {
                case '-':
                    switch(arg.charAt(1)) {
                        case 'd':
                            properties.setProperty(PROP_OUTPUT_DIRECTORY, args[index++].toString());
                            break;
                        case 'i':
                            parseOnly = true;
                            break;
                        case 'p':
                            properties.setProperty(PROP_PACKAGE, args[index++].toString());
                            break;
                        case 's':
                            properties.setProperty(PROP_SUPERCLASS, args[index++].toString());
                            break;
                        case 'v':
                            String spec = getClass().getPackage().getSpecificationVersion();
                            String build = getClass().getPackage().getImplementationVersion();
                            String[] params = { spec, build };
                            session.getChannel(Session.SCREEN_CHANNEL).PRINT(factory.getConstant(MessageFormat.format(COMPILER_USAGE, params)), true, status);
                            return null;
                        default:
                            session.getChannel(Session.SCREEN_CHANNEL).PRINT(factory.getConstant("Unknown option: " + arg), true, status);
                            return null;
                    }
                    break;
                default:
                    files.addElement(arg);
            }
        }
        session.setProperties(properties);
        try {
            if (files.size() > 0) {
                Class walker = Class.forName(session.getProperty(PROP_TREE_WALKER, DEFAULT_TREE_WALKER));
                for (int i = 0; i < files.size(); i++) {
                    MaverickString arg = (MaverickString) files.elementAt(i);
                    Properties properties2 = new Properties(session.getProperties());
                    session.setProperties(properties2);
                    String directory = session.getProperty(PROP_SOURCE_DIRECTORY, DEFAULT_SOURCE_DIRECTORY);
                    java.io.File file = new java.io.File(arg.toString());
                    String parent = file.getParent();
                    if (parent != null) {
                        directory = parent;
                    }
                    SourceReader input = new SourceReader(session, this, properties2, directory);
                    input.push(arg.toString());
                    String source = input.getFilename();
                    properties2.setProperty(PROP_SOURCE_NAME, source);
                    if (!session.getProperty(PROP_CASE_FUNCTIONS, DEFAULT_CASE_FUNCTIONS)) {
                        source = source.toUpperCase();
                    }
                    for (int j = 0; j < BASIC_FILE_SUFFIX.length; j++) {
                        String suffix = BASIC_FILE_SUFFIX[j];
                        if (source.toUpperCase().endsWith(suffix)) {
                            source = source.substring(0, source.length() - suffix.length());
                            break;
                        }
                    }
                    properties2.setProperty(PROP_PROGRAM_NAME, Session.convertName(source));
                    PreprocessorReader reader = new PreprocessorReader(input);
                    reader.setProperties(session.getProperties());
                    BASICLexer lexer = new BASICLexer(reader);
                    lexer.setProperties(session.getProperties());
                    EquateFilterStream efilter = new EquateFilterStream(input, lexer);
                    BASICParser parser = new BASICParser(efilter);
                    parser.setProperties(session.getProperties());
                    parser.program();
                    BasicAST ast = (BasicAST) parser.getAST();
                    if (ast == null) {
                        throw new InternalCompilerException("Error - no output.");
                    }
                    if (parseOnly) {
                        session.getChannel(Session.SCREEN_CHANNEL).PRINT(factory.getConstant(ast.toStringList()), true, status);
                    } else {
                        if (session.getProperty(PROP_DEBUG, DEFAULT_DEBUG)) {
                            AbstractTreeWalker atw = new AbstractTreeWalker();
                            atw.setProperties(session.getProperties());
                            try {
                                atw.program(ast);
                                BasicAST old = ast;
                                ast = (BasicAST) atw.getAST();
                                if (old != null && ast != null) {
                                    compareTree(old, ast);
                                }
                            } catch (Exception e) {
                                e.printStackTrace(System.err);
                            }
                        }
                        ExpressionTreeWalker etw = new ExpressionTreeWalker();
                        etw.setProperties(session.getProperties());
                        etw.program(ast);
                        ast = (BasicAST) etw.getAST();
                        BasicTreeWalker treewalker = (BasicTreeWalker) walker.newInstance();
                        treewalker.setProperties(session.getProperties());
                        ProgramOutputStream pos = new ProgramOutputStream(this, factory, session.getProperties());
                        treewalker.setOutputStream(pos);
                        String programName = session.getProperty(PROP_PROGRAM_NAME, DEFAULT_PROGRAM_NAME);
                        boolean itypemode = session.getProperty(PROP_ITYPE_MODE, DEFAULT_ITYPE_MODE);
                        if (itypemode) {
                            Hashtable identifiers = new Hashtable();
                            getIdentifiers(ast, identifiers);
                            String[] arguments = new String[identifiers.size()];
                            Array array = arg.getArray();
                            ConstantString[] dim = { factory.getConstant(identifiers.size() + 1) };
                            array.DIM(dim);
                            int index2 = 0;
                            for (Enumeration e = identifiers.keys(); e.hasMoreElements(); ) {
                                String arg2 = (String) e.nextElement();
                                arguments[index2++] = arg2;
                                array.get(factory.getConstant(index2 + 1)).set(arg2);
                            }
                            treewalker.setArguments(arguments);
                            treewalker.compile(ast);
                            byte[] classfile = pos.toByteArray();
                            Class c = defineClass(programName, classfile, 0, classfile.length);
                            array.get(factory.getConstant(1)).setProgram((Program) c.newInstance());
                        } else {
                            treewalker.compile(ast);
                        }
                    }
                }
            } else {
                String spec = getClass().getPackage().getSpecificationVersion();
                String build = getClass().getPackage().getImplementationVersion();
                String[] params = { spec, build };
                session.getChannel(Session.SCREEN_CHANNEL).PRINT(factory.getConstant(MessageFormat.format(COMPILER_USAGE, params)), true, status);
                return null;
            }
        } catch (ClassNotFoundException cnfe) {
            session.handleError(cnfe, status);
            exitCode = -1;
        } catch (FileNotFoundException fnfe) {
            session.handleError(fnfe, status);
            exitCode = -1;
        } catch (IllegalAccessException iae) {
            session.handleError(iae, status);
            exitCode = -1;
        } catch (InstantiationException ie) {
            session.handleError(ie, status);
            exitCode = -1;
        } catch (IOException ioe) {
            session.handleError(ioe, status);
            exitCode = -1;
        } catch (InternalCompilerException ice) {
            session.handleError(ice, status);
            exitCode = -1;
        } catch (RecognitionException re) {
            session.handleError(re, status);
            exitCode = -1;
        } catch (TokenStreamException tse) {
            session.handleError(tse, status);
            exitCode = -1;
        } catch (Exception e) {
            session.handleError(e, status);
            exitCode = -1;
        } finally {
            session.setProperties(orig);
        }
        return null;
    }

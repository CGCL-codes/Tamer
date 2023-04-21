    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java Evaluator infile");
            System.exit(1);
        }
        try {
            java.io.InputStream is = new java.io.FileInputStream(args[0]);
            Kitsch.yyInput scanner = new Kitsch.yyLex(is);
            java.lang.Object parser = scanner.getClass().getEnclosingClass().newInstance();
            java.lang.Object tree = parser.getClass().getMethod("yyparse", new java.lang.Class[] { Kitsch.yyInput.class, java.lang.Object[].class }).invoke(parser, scanner, new java.lang.Object[] { new Kitsch.yyTree() });
            FuncVisitor fv = new FuncVisitor();
            Map<FuncVisitor.Function, Kitsch.yyTree.FuncDecl> funcsToNodes = (Map) fv.visit((Kitsch.yyTree.Prgm) tree);
            TypeChecker checker = new TypeChecker(funcsToNodes);
            checker.visit((Kitsch.yyTree.Prgm) tree);
            Evaluator eval = new Evaluator(funcsToNodes);
            eval.visit((Kitsch.yyTree.Prgm) tree);
        } catch (java.io.FileNotFoundException ex) {
            System.err.println(args[0] + ": File does not exist.");
            System.exit(1);
        } catch (java.lang.InstantiationException e) {
            System.err.println("cannot create parser [" + e + "]");
            System.exit(1);
        } catch (java.lang.IllegalAccessException e) {
            System.err.println("cannot create parser [" + e + "]");
            System.exit(1);
        } catch (java.lang.NoSuchMethodException e) {
            System.err.println("cannot create parser [" + e + "]");
            System.exit(1);
        } catch (java.lang.reflect.InvocationTargetException e) {
            System.err.println("parse error [" + e + "]");
            System.exit(1);
        } catch (Exception e) {
            System.exit(1);
        }
    }

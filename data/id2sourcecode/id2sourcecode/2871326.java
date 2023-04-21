    private boolean loadPhpAst(IFile file, RefactoringStatus status) {
        boolean result = false;
        if (file == null || !file.exists()) {
            Log.write(net.sourceforge.refactor4pdt.log.Log.Level.FATAL_ERROR, getClass(), "loadPhpAst", "no source file");
            status.addFatalError(Messages.Scope_NoSourceFile);
        } else if (file.isReadOnly()) {
            Log.write(net.sourceforge.refactor4pdt.log.Log.Level.FATAL_ERROR, getClass(), "loadPhpAst", "read only");
            status.addFatalError(Messages.Scope_ReadOnly);
        } else {
            try {
                String content = readFileContent(file, status);
                ASTParser parser = ASTParser.newParser(new InputStreamReader(file.getContents()), ASTParser.VERSION_PHP5, false);
                rootNode.put(file, parser.createAST(null));
                documents.put(file, new Document(content));
                Log.write(net.sourceforge.refactor4pdt.log.Log.Level.INFO_L2, getClass(), "loadPhpAst", (new StringBuilder("loadPhpAst: \n\n")).append(rootNode.get(file).toString()).append("\n").toString());
                result = true;
            } catch (Exception e) {
                Log.write(net.sourceforge.refactor4pdt.log.Log.Level.FATAL_ERROR, getClass(), "getLine", "Error in ASTParser.parse", e);
                status.addFatalError(Messages.Scope_ParserError);
            }
        }
        return result;
    }

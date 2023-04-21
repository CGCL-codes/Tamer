    public RefactoringStatus runVisitors(RefactoringStatus status) {
        loadAllPhpAst(status);
        for (Iterator iterator = getLoadedFiles().iterator(); iterator.hasNext(); ) {
            IFile file = (IFile) iterator.next();
            if (!visitors.containsKey(file)) {
                PhpRefactoringVisitor visitor = info.getFactory().makeVisitor();
                if (visitor == null) {
                    Log.write(net.sourceforge.refactor4pdt.log.Log.Level.ERROR, getClass(), "runVisitors", "visitor is null!!");
                    status.addFatalError(Messages.Scope_VisitorIsNull);
                } else {
                    getRootNode(file).traverseTopDown(visitor);
                    visitors.put(file, visitor);
                    Log.write(net.sourceforge.refactor4pdt.log.Log.Level.INFO_L1, getClass(), "runVisitors", (new StringBuilder("Visitor should be loaded!!! file: ")).append(file.getName()).toString());
                }
            } else {
                Log.write(net.sourceforge.refactor4pdt.log.Log.Level.INFO_L3, getClass(), "runVisitors", "visitor already loaded!");
            }
        }
        return status;
    }

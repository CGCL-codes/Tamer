    private boolean privateAndNotExcluded(MethodNameDeclaration mnd) {
        ASTMethodDeclarator node = (ASTMethodDeclarator) mnd.getNode();
        return ((AccessNode) node.jjtGetParent()).isPrivate() && !node.hasImageEqualTo("readObject") && !node.hasImageEqualTo("writeObject") && !node.hasImageEqualTo("readResolve") && !node.hasImageEqualTo("writeReplace");
    }

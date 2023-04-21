    public int match(FieldDeclaration node, MatchingNodeSet nodeSet) {
        int referencesLevel = IMPOSSIBLE_MATCH;
        if (this.pattern.findReferences) if (this.pattern.writeAccess && !this.pattern.readAccess && node.initialization != null) if (matchesName(this.pattern.name, node.name)) referencesLevel = ((InternalSearchPattern) this.pattern).mustResolve ? POSSIBLE_MATCH : ACCURATE_MATCH;
        int declarationsLevel = IMPOSSIBLE_MATCH;
        if (this.pattern.findDeclarations) {
            switch(node.getKind()) {
                case AbstractVariableDeclaration.FIELD:
                case AbstractVariableDeclaration.ENUM_CONSTANT:
                    if (matchesName(this.pattern.name, node.name)) if (matchesTypeReference(((FieldPattern) this.pattern).typeSimpleName, node.type)) declarationsLevel = ((InternalSearchPattern) this.pattern).mustResolve ? POSSIBLE_MATCH : ACCURATE_MATCH;
                    break;
            }
        }
        return nodeSet.addMatch(node, referencesLevel >= declarationsLevel ? referencesLevel : declarationsLevel);
    }

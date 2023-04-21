    public int match(LocalDeclaration node, MatchingNodeSet nodeSet) {
        int referencesLevel = IMPOSSIBLE_MATCH;
        if (this.pattern.findReferences) if (this.pattern.writeAccess && !this.pattern.readAccess && node.initialization != null) if (matchesName(this.pattern.name, node.name)) referencesLevel = ((InternalSearchPattern) this.pattern).mustResolve ? POSSIBLE_MATCH : ACCURATE_MATCH;
        int declarationsLevel = IMPOSSIBLE_MATCH;
        if (this.pattern.findDeclarations) if (matchesName(this.pattern.name, node.name)) if (node.declarationSourceStart == getLocalVariable().declarationSourceStart) declarationsLevel = ((InternalSearchPattern) this.pattern).mustResolve ? POSSIBLE_MATCH : ACCURATE_MATCH;
        return nodeSet.addMatch(node, referencesLevel >= declarationsLevel ? referencesLevel : declarationsLevel);
    }

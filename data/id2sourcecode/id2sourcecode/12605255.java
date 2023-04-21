    public java.util.List<org.emftext.language.OCL.resource.OCL.mopp.OCLExpectedTerminal> parseToExpectedElements(org.eclipse.emf.ecore.EClass type, org.emftext.language.OCL.resource.OCL.IOCLTextResource dummyResource, int cursorOffset) {
        this.rememberExpectedElements = true;
        this.parseToIndexTypeObject = type;
        this.cursorOffset = cursorOffset;
        this.lastStartIncludingHidden = -1;
        final org.antlr.runtime3_3_0.CommonTokenStream tokenStream = (org.antlr.runtime3_3_0.CommonTokenStream) getTokenStream();
        org.emftext.language.OCL.resource.OCL.IOCLParseResult result = parse();
        for (org.eclipse.emf.ecore.EObject incompleteObject : incompleteObjects) {
            org.antlr.runtime3_3_0.Lexer lexer = (org.antlr.runtime3_3_0.Lexer) tokenStream.getTokenSource();
            int endChar = lexer.getCharIndex();
            int endLine = lexer.getLine();
            setLocalizationEnd(result.getPostParseCommands(), incompleteObject, endChar, endLine);
        }
        if (result != null) {
            org.eclipse.emf.ecore.EObject root = result.getRoot();
            if (root != null) {
                dummyResource.getContentsInternal().add(root);
            }
            for (org.emftext.language.OCL.resource.OCL.IOCLCommand<org.emftext.language.OCL.resource.OCL.IOCLTextResource> command : result.getPostParseCommands()) {
                command.execute(dummyResource);
            }
        }
        expectedElements = expectedElements.subList(0, expectedElementsIndexOfLastCompleteElement + 1);
        int lastFollowSetID = expectedElements.get(expectedElementsIndexOfLastCompleteElement).getFollowSetID();
        java.util.Set<org.emftext.language.OCL.resource.OCL.mopp.OCLExpectedTerminal> currentFollowSet = new java.util.LinkedHashSet<org.emftext.language.OCL.resource.OCL.mopp.OCLExpectedTerminal>();
        java.util.List<org.emftext.language.OCL.resource.OCL.mopp.OCLExpectedTerminal> newFollowSet = new java.util.ArrayList<org.emftext.language.OCL.resource.OCL.mopp.OCLExpectedTerminal>();
        for (int i = expectedElementsIndexOfLastCompleteElement; i >= 0; i--) {
            org.emftext.language.OCL.resource.OCL.mopp.OCLExpectedTerminal expectedElementI = expectedElements.get(i);
            if (expectedElementI.getFollowSetID() == lastFollowSetID) {
                currentFollowSet.add(expectedElementI);
            } else {
                break;
            }
        }
        int followSetID = 25;
        int i;
        for (i = tokenIndexOfLastCompleteElement; i < tokenStream.size(); i++) {
            org.antlr.runtime3_3_0.CommonToken nextToken = (org.antlr.runtime3_3_0.CommonToken) tokenStream.get(i);
            if (nextToken.getType() < 0) {
                break;
            }
            if (nextToken.getChannel() == 99) {
            } else {
                for (org.emftext.language.OCL.resource.OCL.mopp.OCLExpectedTerminal nextFollow : newFollowSet) {
                    lastTokenIndex = 0;
                    setPosition(nextFollow, i);
                }
                newFollowSet.clear();
                for (org.emftext.language.OCL.resource.OCL.mopp.OCLExpectedTerminal nextFollow : currentFollowSet) {
                    if (nextFollow.getTerminal().getTokenNames().contains(getTokenNames()[nextToken.getType()])) {
                        java.util.Collection<org.emftext.language.OCL.resource.OCL.util.OCLPair<org.emftext.language.OCL.resource.OCL.IOCLExpectedElement, org.eclipse.emf.ecore.EStructuralFeature[]>> newFollowers = nextFollow.getTerminal().getFollowers();
                        for (org.emftext.language.OCL.resource.OCL.util.OCLPair<org.emftext.language.OCL.resource.OCL.IOCLExpectedElement, org.eclipse.emf.ecore.EStructuralFeature[]> newFollowerPair : newFollowers) {
                            org.emftext.language.OCL.resource.OCL.IOCLExpectedElement newFollower = newFollowerPair.getLeft();
                            org.emftext.language.OCL.resource.OCL.mopp.OCLExpectedTerminal newFollowTerminal = new org.emftext.language.OCL.resource.OCL.mopp.OCLExpectedTerminal(newFollower, followSetID, newFollowerPair.getRight());
                            newFollowSet.add(newFollowTerminal);
                            expectedElements.add(newFollowTerminal);
                        }
                    }
                }
                currentFollowSet.clear();
                currentFollowSet.addAll(newFollowSet);
            }
            followSetID++;
        }
        for (org.emftext.language.OCL.resource.OCL.mopp.OCLExpectedTerminal nextFollow : newFollowSet) {
            lastTokenIndex = 0;
            setPosition(nextFollow, i);
        }
        return this.expectedElements;
    }

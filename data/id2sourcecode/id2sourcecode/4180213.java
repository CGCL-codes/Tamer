    public java.util.List<fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.mopp.Ocl4tstExpectedTerminal> parseToExpectedElements(org.eclipse.emf.ecore.EClass type, fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.IOcl4tstTextResource dummyResource, int cursorOffset) {
        this.rememberExpectedElements = true;
        this.parseToIndexTypeObject = type;
        this.cursorOffset = cursorOffset;
        this.lastStartIncludingHidden = -1;
        final org.antlr.runtime3_3_0.CommonTokenStream tokenStream = (org.antlr.runtime3_3_0.CommonTokenStream) getTokenStream();
        fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.IOcl4tstParseResult result = parse();
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
            for (fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.IOcl4tstCommand<fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.IOcl4tstTextResource> command : result.getPostParseCommands()) {
                command.execute(dummyResource);
            }
        }
        expectedElements = expectedElements.subList(0, expectedElementsIndexOfLastCompleteElement + 1);
        int lastFollowSetID = expectedElements.get(expectedElementsIndexOfLastCompleteElement).getFollowSetID();
        java.util.Set<fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.mopp.Ocl4tstExpectedTerminal> currentFollowSet = new java.util.LinkedHashSet<fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.mopp.Ocl4tstExpectedTerminal>();
        java.util.List<fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.mopp.Ocl4tstExpectedTerminal> newFollowSet = new java.util.ArrayList<fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.mopp.Ocl4tstExpectedTerminal>();
        for (int i = expectedElementsIndexOfLastCompleteElement; i >= 0; i--) {
            fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.mopp.Ocl4tstExpectedTerminal expectedElementI = expectedElements.get(i);
            if (expectedElementI.getFollowSetID() == lastFollowSetID) {
                currentFollowSet.add(expectedElementI);
            } else {
                break;
            }
        }
        int followSetID = 21;
        int i;
        for (i = tokenIndexOfLastCompleteElement; i < tokenStream.size(); i++) {
            org.antlr.runtime3_3_0.CommonToken nextToken = (org.antlr.runtime3_3_0.CommonToken) tokenStream.get(i);
            if (nextToken.getType() < 0) {
                break;
            }
            if (nextToken.getChannel() == 99) {
            } else {
                for (fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.mopp.Ocl4tstExpectedTerminal nextFollow : newFollowSet) {
                    lastTokenIndex = 0;
                    setPosition(nextFollow, i);
                }
                newFollowSet.clear();
                for (fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.mopp.Ocl4tstExpectedTerminal nextFollow : currentFollowSet) {
                    if (nextFollow.getTerminal().getTokenNames().contains(getTokenNames()[nextToken.getType()])) {
                        java.util.Collection<fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.util.Ocl4tstPair<fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.IOcl4tstExpectedElement, org.eclipse.emf.ecore.EStructuralFeature[]>> newFollowers = nextFollow.getTerminal().getFollowers();
                        for (fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.util.Ocl4tstPair<fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.IOcl4tstExpectedElement, org.eclipse.emf.ecore.EStructuralFeature[]> newFollowerPair : newFollowers) {
                            fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.IOcl4tstExpectedElement newFollower = newFollowerPair.getLeft();
                            fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.mopp.Ocl4tstExpectedTerminal newFollowTerminal = new fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.mopp.Ocl4tstExpectedTerminal(newFollower, followSetID, newFollowerPair.getRight());
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
        for (fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.mopp.Ocl4tstExpectedTerminal nextFollow : newFollowSet) {
            lastTokenIndex = 0;
            setPosition(nextFollow, i);
        }
        return this.expectedElements;
    }

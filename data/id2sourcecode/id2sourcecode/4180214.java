    public void setPosition(fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.mopp.Ocl4tstExpectedTerminal expectedElement, int tokenIndex) {
        int currentIndex = Math.max(0, tokenIndex);
        for (int index = lastTokenIndex; index < currentIndex; index++) {
            if (index >= input.size()) {
                break;
            }
            org.antlr.runtime3_3_0.CommonToken tokenAtIndex = (org.antlr.runtime3_3_0.CommonToken) input.get(index);
            stopIncludingHiddenTokens = tokenAtIndex.getStopIndex() + 1;
            if (tokenAtIndex.getChannel() != 99) {
                stopExcludingHiddenTokens = tokenAtIndex.getStopIndex() + 1;
            }
        }
        lastTokenIndex = Math.max(0, currentIndex);
        expectedElement.setPosition(stopExcludingHiddenTokens, stopIncludingHiddenTokens);
    }

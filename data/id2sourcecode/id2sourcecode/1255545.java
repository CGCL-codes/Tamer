    public void addExpectedElement(org.emftext.language.models.resource.model.IModelExpectedElement expectedElement, java.lang.String message) {
        if (!this.rememberExpectedElements) {
            return;
        }
        if (this.reachedIndex) {
            return;
        }
        int currentIndex = java.lang.Math.max(0, input.index());
        for (int index = lastTokenIndex; index < currentIndex; index++) {
            if (index >= input.size()) {
                break;
            }
            org.antlr.runtime.CommonToken tokenAtIndex = (org.antlr.runtime.CommonToken) input.get(index);
            stopIncludingHiddenTokens = tokenAtIndex.getStopIndex() + 1;
            if (tokenAtIndex.getChannel() != 99) {
                stopExcludingHiddenTokens = tokenAtIndex.getStopIndex() + 1;
            }
        }
        lastTokenIndex = java.lang.Math.max(0, currentIndex);
        expectedElement.setPosition(stopExcludingHiddenTokens, stopIncludingHiddenTokens);
        System.out.println("Adding expected element (" + message + "): " + expectedElement + "");
        this.expectedElements.add(expectedElement);
    }

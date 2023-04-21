    protected void retrieveLayoutInformation(org.eclipse.emf.ecore.EObject element, org.emftext.language.OCL.resource.OCL.grammar.OCLSyntaxElement syntaxElement, Object object, boolean ignoreTokensAfterLastVisibleToken) {
        if (element == null) {
            return;
        }
        boolean isElementToStore = syntaxElement == null;
        isElementToStore |= syntaxElement instanceof org.emftext.language.OCL.resource.OCL.grammar.OCLPlaceholder;
        isElementToStore |= syntaxElement instanceof org.emftext.language.OCL.resource.OCL.grammar.OCLKeyword;
        isElementToStore |= syntaxElement instanceof org.emftext.language.OCL.resource.OCL.grammar.OCLEnumerationTerminal;
        isElementToStore |= syntaxElement instanceof org.emftext.language.OCL.resource.OCL.grammar.OCLBooleanTerminal;
        if (!isElementToStore) {
            return;
        }
        org.emftext.language.OCL.resource.OCL.mopp.OCLLayoutInformationAdapter layoutInformationAdapter = getLayoutInformationAdapter(element);
        for (org.antlr.runtime3_3_0.CommonToken anonymousToken : anonymousTokens) {
            layoutInformationAdapter.addLayoutInformation(new org.emftext.language.OCL.resource.OCL.mopp.OCLLayoutInformation(syntaxElement, object, anonymousToken.getStartIndex(), anonymousToken.getText(), null));
        }
        anonymousTokens.clear();
        int currentPos = getTokenStream().index();
        if (currentPos == 0) {
            return;
        }
        int endPos = currentPos - 1;
        if (ignoreTokensAfterLastVisibleToken) {
            for (; endPos >= this.lastPosition2; endPos--) {
                org.antlr.runtime3_3_0.Token token = getTokenStream().get(endPos);
                int _channel = token.getChannel();
                if (_channel != 99) {
                    break;
                }
            }
        }
        StringBuilder hiddenTokenText = new StringBuilder();
        StringBuilder visibleTokenText = new StringBuilder();
        org.antlr.runtime3_3_0.CommonToken firstToken = null;
        for (int pos = this.lastPosition2; pos <= endPos; pos++) {
            org.antlr.runtime3_3_0.Token token = getTokenStream().get(pos);
            if (firstToken == null) {
                firstToken = (org.antlr.runtime3_3_0.CommonToken) token;
            }
            int _channel = token.getChannel();
            if (_channel == 99) {
                hiddenTokenText.append(token.getText());
            } else {
                visibleTokenText.append(token.getText());
            }
        }
        int offset = -1;
        if (firstToken != null) {
            offset = firstToken.getStartIndex();
        }
        layoutInformationAdapter.addLayoutInformation(new org.emftext.language.OCL.resource.OCL.mopp.OCLLayoutInformation(syntaxElement, object, offset, hiddenTokenText.toString(), visibleTokenText.toString()));
        this.lastPosition2 = (endPos < 0 ? 0 : endPos + 1);
    }

    protected void collectHiddenTokens(org.eclipse.emf.ecore.EObject element) {
        int currentPos = getTokenStream().index();
        if (currentPos == 0) {
            return;
        }
        int endPos = currentPos - 1;
        for (; endPos >= lastPosition; endPos--) {
            org.antlr.runtime.Token token = getTokenStream().get(endPos);
            int _channel = token.getChannel();
            if (_channel != 99) {
                break;
            }
        }
        for (int pos = lastPosition; pos < endPos; pos++) {
            org.antlr.runtime.Token token = getTokenStream().get(pos);
            int _channel = token.getChannel();
            if (_channel == 99) {
                if (token.getType() == ModelLexer.SL_COMMENT) {
                    org.eclipse.emf.ecore.EStructuralFeature feature = element.eClass().getEStructuralFeature("comments");
                    if (feature != null) {
                        org.emftext.language.models.resource.model.IModelTokenResolver resolvedResolver = tokenResolverFactory.createCollectInTokenResolver("comments");
                        resolvedResolver.setOptions(getOptions());
                        org.emftext.language.models.resource.model.IModelTokenResolveResult resolvedResult = getFreshTokenResolveResult();
                        resolvedResolver.resolve(token.getText(), feature, resolvedResult);
                        java.lang.Object resolvedObject = resolvedResult.getResolvedToken();
                        if (resolvedObject == null) {
                            addErrorToResource(resolvedResult.getErrorMessage(), ((org.antlr.runtime.CommonToken) token).getLine(), ((org.antlr.runtime.CommonToken) token).getCharPositionInLine(), ((org.antlr.runtime.CommonToken) token).getStartIndex(), ((org.antlr.runtime.CommonToken) token).getStopIndex());
                        }
                        if (java.lang.String.class.isInstance(resolvedObject)) {
                            ((java.util.List) element.eGet(feature)).add((java.lang.String) resolvedObject);
                        } else {
                            System.out.println("WARNING: Attribute comments for token " + token + " has wrong type in element " + element + " (expected java.lang.String).");
                        }
                    } else {
                        System.out.println("WARNING: Attribute comments for token " + token + " was not found in element " + element + ".");
                    }
                }
                if (token.getType() == ModelLexer.ML_COMMENT) {
                    org.eclipse.emf.ecore.EStructuralFeature feature = element.eClass().getEStructuralFeature("comments");
                    if (feature != null) {
                        org.emftext.language.models.resource.model.IModelTokenResolver resolvedResolver = tokenResolverFactory.createCollectInTokenResolver("comments");
                        resolvedResolver.setOptions(getOptions());
                        org.emftext.language.models.resource.model.IModelTokenResolveResult resolvedResult = getFreshTokenResolveResult();
                        resolvedResolver.resolve(token.getText(), feature, resolvedResult);
                        java.lang.Object resolvedObject = resolvedResult.getResolvedToken();
                        if (resolvedObject == null) {
                            addErrorToResource(resolvedResult.getErrorMessage(), ((org.antlr.runtime.CommonToken) token).getLine(), ((org.antlr.runtime.CommonToken) token).getCharPositionInLine(), ((org.antlr.runtime.CommonToken) token).getStartIndex(), ((org.antlr.runtime.CommonToken) token).getStopIndex());
                        }
                        if (java.lang.String.class.isInstance(resolvedObject)) {
                            ((java.util.List) element.eGet(feature)).add((java.lang.String) resolvedObject);
                        } else {
                            System.out.println("WARNING: Attribute comments for token " + token + " has wrong type in element " + element + " (expected java.lang.String).");
                        }
                    } else {
                        System.out.println("WARNING: Attribute comments for token " + token + " was not found in element " + element + ".");
                    }
                }
            }
        }
        lastPosition = (endPos < 0 ? 0 : endPos);
    }

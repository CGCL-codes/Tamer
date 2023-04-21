    private TagletOutput getInheritedTagletOutput(boolean isNonTypeParams, Doc holder, TagletWriter writer, Object[] formalParameters, Set alreadyDocumented) {
        TagletOutput result = writer.getOutputInstance();
        if ((!alreadyDocumented.contains(null)) && holder instanceof MethodDoc) {
            for (int i = 0; i < formalParameters.length; i++) {
                if (alreadyDocumented.contains(String.valueOf(i))) {
                    continue;
                }
                DocFinder.Output inheritedDoc = DocFinder.search(new DocFinder.Input((MethodDoc) holder, this, String.valueOf(i), !isNonTypeParams));
                if (inheritedDoc.inlineTags != null && inheritedDoc.inlineTags.length > 0) {
                    result.appendOutput(processParamTag(isNonTypeParams, writer, (ParamTag) inheritedDoc.holderTag, isNonTypeParams ? ((Parameter) formalParameters[i]).name() : ((TypeVariable) formalParameters[i]).typeName(), alreadyDocumented.size() == 0));
                }
                alreadyDocumented.add(String.valueOf(i));
            }
        }
        return result;
    }

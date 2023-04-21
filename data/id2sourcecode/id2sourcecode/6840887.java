    private TagletOutput getTagletOutput(boolean isNonTypeParams, Doc holder, TagletWriter writer, Object[] formalParameters, ParamTag[] paramTags) {
        TagletOutput result = writer.getOutputInstance();
        Set alreadyDocumented = new HashSet();
        if (paramTags.length > 0) {
            result.appendOutput(processParamTags(isNonTypeParams, paramTags, getRankMap(formalParameters), writer, alreadyDocumented));
        }
        if (alreadyDocumented.size() != formalParameters.length) {
            result.appendOutput(getInheritedTagletOutput(isNonTypeParams, holder, writer, formalParameters, alreadyDocumented));
        }
        return result;
    }

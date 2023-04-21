    private TagletOutput processParamTags(boolean isNonTypeParams, ParamTag[] paramTags, Map rankMap, TagletWriter writer, Set alreadyDocumented) {
        TagletOutput result = writer.getOutputInstance();
        if (paramTags.length > 0) {
            for (int i = 0; i < paramTags.length; ++i) {
                ParamTag pt = paramTags[i];
                String paramName = isNonTypeParams ? pt.parameterName() : "<" + pt.parameterName() + ">";
                if (!rankMap.containsKey(pt.parameterName())) {
                    writer.getMsgRetriever().warning(pt.position(), isNonTypeParams ? "doclet.Parameters_warn" : "doclet.Type_Parameters_warn", paramName);
                }
                String rank = (String) rankMap.get(pt.parameterName());
                if (rank != null && alreadyDocumented.contains(rank)) {
                    writer.getMsgRetriever().warning(pt.position(), isNonTypeParams ? "doclet.Parameters_dup_warn" : "doclet.Type_Parameters_dup_warn", paramName);
                }
                result.appendOutput(processParamTag(isNonTypeParams, writer, pt, pt.parameterName(), alreadyDocumented.size() == 0));
                alreadyDocumented.add(rank);
            }
        }
        return result;
    }

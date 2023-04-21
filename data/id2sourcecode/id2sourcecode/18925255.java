    protected TagletOutput throwsTagsOutput(ThrowsTag[] throwTags, TagletWriter writer, Set alreadyDocumented, boolean allowDups) {
        TagletOutput result = writer.getOutputInstance();
        if (throwTags.length > 0) {
            for (int i = 0; i < throwTags.length; ++i) {
                ThrowsTag tt = throwTags[i];
                ClassDoc cd = tt.exception();
                if ((!allowDups) && (alreadyDocumented.contains(tt.exceptionName()) || (cd != null && alreadyDocumented.contains(cd.qualifiedName())))) {
                    continue;
                }
                if (alreadyDocumented.size() == 0) {
                    result.appendOutput(writer.getThrowsHeader());
                }
                result.appendOutput(writer.throwsTagOutput(tt));
                alreadyDocumented.add(cd != null ? cd.qualifiedName() : tt.exceptionName());
            }
        }
        return result;
    }

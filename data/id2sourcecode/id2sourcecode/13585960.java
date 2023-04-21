    private TagletOutput inheritThrowsDocumentation(Doc holder, Type[] declaredExceptionTypes, Set alreadyDocumented, TagletWriter writer) {
        TagletOutput result = writer.getOutputInstance();
        if (holder instanceof MethodDoc) {
            Set declaredExceptionTags = new LinkedHashSet();
            for (int j = 0; j < declaredExceptionTypes.length; j++) {
                DocFinder.Output inheritedDoc = DocFinder.search(new DocFinder.Input((MethodDoc) holder, this, declaredExceptionTypes[j].typeName()));
                if (inheritedDoc.tagList.size() == 0) {
                    inheritedDoc = DocFinder.search(new DocFinder.Input((MethodDoc) holder, this, declaredExceptionTypes[j].qualifiedTypeName()));
                }
                declaredExceptionTags.addAll(inheritedDoc.tagList);
            }
            result.appendOutput(throwsTagsOutput((ThrowsTag[]) declaredExceptionTags.toArray(new ThrowsTag[] {}), writer, alreadyDocumented, false));
        }
        return result;
    }

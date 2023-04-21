    private TagletOutput linkToUndocumentedDeclaredExceptions(Type[] declaredExceptionTypes, Set alreadyDocumented, TagletWriter writer) {
        TagletOutput result = writer.getOutputInstance();
        for (int i = 0; i < declaredExceptionTypes.length; i++) {
            if (declaredExceptionTypes[i].asClassDoc() != null && !alreadyDocumented.contains(declaredExceptionTypes[i].asClassDoc().name()) && !alreadyDocumented.contains(declaredExceptionTypes[i].asClassDoc().qualifiedName())) {
                if (alreadyDocumented.size() == 0) {
                    result.appendOutput(writer.getThrowsHeader());
                }
                result.appendOutput(writer.throwsTagOutput(declaredExceptionTypes[i]));
                alreadyDocumented.add(declaredExceptionTypes[i].asClassDoc().name());
            }
        }
        return result;
    }

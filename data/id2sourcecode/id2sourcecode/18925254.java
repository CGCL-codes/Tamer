    public TagletOutput getTagletOutput(Doc holder, TagletWriter writer) {
        ExecutableMemberDoc execHolder = (ExecutableMemberDoc) holder;
        ThrowsTag[] tags = execHolder.throwsTags();
        TagletOutput result = writer.getOutputInstance();
        HashSet alreadyDocumented = new HashSet();
        if (tags.length > 0) {
            result.appendOutput(throwsTagsOutput(execHolder.throwsTags(), writer, alreadyDocumented, true));
        }
        result.appendOutput(inheritThrowsDocumentation(holder, execHolder.thrownExceptionTypes(), alreadyDocumented, writer));
        result.appendOutput(linkToUndocumentedDeclaredExceptions(execHolder.thrownExceptionTypes(), alreadyDocumented, writer));
        return result;
    }

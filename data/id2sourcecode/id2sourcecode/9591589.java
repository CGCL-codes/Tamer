    private static SearchPattern createFieldPattern(String patternString, int limitTo, int matchRule) {
        String fieldName = patternString;
        if (fieldName == null) return null;
        char[] fieldNameChars = fieldName.toCharArray();
        if (fieldNameChars.length == 1 && fieldNameChars[0] == '*') fieldNameChars = null;
        char[] declaringTypeQualification = null, declaringTypeSimpleName = null;
        char[] typeQualification = null, typeSimpleName = null;
        boolean findDeclarations = false;
        boolean readAccess = false;
        boolean writeAccess = false;
        switch(limitTo) {
            case IRubySearchConstants.DECLARATIONS:
                findDeclarations = true;
                break;
            case IRubySearchConstants.REFERENCES:
                readAccess = true;
                writeAccess = true;
                break;
            case IRubySearchConstants.READ_ACCESSES:
                readAccess = true;
                break;
            case IRubySearchConstants.WRITE_ACCESSES:
                writeAccess = true;
                break;
            case IRubySearchConstants.ALL_OCCURRENCES:
                findDeclarations = true;
                readAccess = true;
                writeAccess = true;
                break;
        }
        return new FieldPattern(findDeclarations, readAccess, writeAccess, fieldNameChars, declaringTypeQualification, declaringTypeSimpleName, matchRule);
    }

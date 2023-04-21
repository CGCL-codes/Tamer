    public static SearchPattern createPattern(IRubyElement element, int limitTo, int matchRule) {
        SearchPattern searchPattern = null;
        int lastDot;
        boolean ignoreDeclaringType = true;
        int maskedLimitTo = limitTo;
        if (maskedLimitTo == IRubySearchConstants.DECLARATIONS || maskedLimitTo == IRubySearchConstants.ALL_OCCURRENCES) {
            ignoreDeclaringType = (limitTo & IRubySearchConstants.IGNORE_DECLARING_TYPE) != 0;
        }
        char[] declaringSimpleName = null;
        char[] declaringQualification = null;
        switch(element.getElementType()) {
            case IRubyElement.FIELD:
            case IRubyElement.INSTANCE_VAR:
            case IRubyElement.CONSTANT:
            case IRubyElement.CLASS_VAR:
                IField field = (IField) element;
                if (!ignoreDeclaringType) {
                    IType declaringClass = field.getDeclaringType();
                    declaringSimpleName = declaringClass.getElementName().toCharArray();
                    declaringQualification = declaringClass.getSourceFolder().getElementName().toCharArray();
                    char[][] enclosingNames = enclosingTypeNames(declaringClass);
                    if (enclosingNames.length > 0) {
                        declaringQualification = CharOperation.concat(declaringQualification, CharOperation.concatWith(enclosingNames, '.'), '.');
                    }
                }
                char[] name = field.getElementName().toCharArray();
                boolean findDeclarations = false;
                boolean readAccess = false;
                boolean writeAccess = false;
                switch(maskedLimitTo) {
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
                searchPattern = new FieldPattern(findDeclarations, readAccess, writeAccess, name, declaringQualification, declaringSimpleName, matchRule);
                break;
            case IRubyElement.IMPORT_DECLARATION:
                String elementName = element.getElementName();
                lastDot = elementName.lastIndexOf('.');
                if (lastDot == -1) return null;
                IImportDeclaration importDecl = (IImportDeclaration) element;
                searchPattern = createTypePattern(elementName.substring(lastDot + 1).toCharArray(), elementName.substring(0, lastDot).toCharArray(), null, null, null, maskedLimitTo, matchRule);
                break;
            case IRubyElement.LOCAL_VARIABLE:
                LocalVariable localVar = (LocalVariable) element;
                boolean findVarDeclarations = false;
                boolean findVarReadAccess = false;
                boolean findVarWriteAccess = false;
                switch(maskedLimitTo) {
                    case IRubySearchConstants.DECLARATIONS:
                        findVarDeclarations = true;
                        break;
                    case IRubySearchConstants.REFERENCES:
                        findVarReadAccess = true;
                        findVarWriteAccess = true;
                        break;
                    case IRubySearchConstants.READ_ACCESSES:
                        findVarReadAccess = true;
                        break;
                    case IRubySearchConstants.WRITE_ACCESSES:
                        findVarWriteAccess = true;
                        break;
                    case IRubySearchConstants.ALL_OCCURRENCES:
                        findVarDeclarations = true;
                        findVarReadAccess = true;
                        findVarWriteAccess = true;
                        break;
                }
                searchPattern = new LocalVariablePattern(findVarDeclarations, findVarReadAccess, findVarWriteAccess, localVar, matchRule);
                break;
            case IRubyElement.METHOD:
                IMethod method = (IMethod) element;
                boolean isConstructor = method.isConstructor();
                IType declaringClass = method.getDeclaringType();
                if (ignoreDeclaringType) {
                    if (isConstructor) declaringSimpleName = declaringClass.getElementName().toCharArray();
                } else {
                    declaringSimpleName = declaringClass.getElementName().toCharArray();
                    declaringQualification = declaringClass.getSourceFolder().getElementName().toCharArray();
                    char[][] enclosingNames = enclosingTypeNames(declaringClass);
                    if (enclosingNames.length > 0) {
                        declaringQualification = CharOperation.concat(declaringQualification, CharOperation.concatWith(enclosingNames, '.'), '.');
                    }
                }
                char[] selector = method.getElementName().toCharArray();
                String[] parameterNames;
                try {
                    parameterNames = method.getParameterNames();
                } catch (RubyModelException e) {
                    return null;
                }
                int paramCount = parameterNames.length;
                char[][] parameterSimpleNames = new char[paramCount][];
                for (int i = 0; i < paramCount; i++) {
                    parameterSimpleNames[i] = parameterNames[i].toCharArray();
                }
                boolean findMethodDeclarations = true;
                boolean findMethodReferences = true;
                switch(maskedLimitTo) {
                    case IRubySearchConstants.DECLARATIONS:
                        findMethodReferences = false;
                        break;
                    case IRubySearchConstants.REFERENCES:
                        findMethodDeclarations = false;
                        break;
                    case IRubySearchConstants.ALL_OCCURRENCES:
                        break;
                }
                if (isConstructor) {
                    searchPattern = new ConstructorPattern(findMethodDeclarations, findMethodReferences, declaringSimpleName, declaringQualification, parameterSimpleNames, method, matchRule);
                } else {
                    searchPattern = new MethodPattern(findMethodDeclarations, findMethodReferences, selector, declaringQualification, declaringSimpleName, parameterSimpleNames, method, matchRule);
                }
                break;
            case IRubyElement.TYPE:
                IType type = (IType) element;
                searchPattern = createTypePattern(type.getElementName().toCharArray(), type.getSourceFolder().getElementName().toCharArray(), ignoreDeclaringType ? null : enclosingTypeNames(type), null, type, maskedLimitTo, matchRule);
                break;
        }
        if (searchPattern != null) MatchLocator.setFocus(searchPattern, element);
        return searchPattern;
    }

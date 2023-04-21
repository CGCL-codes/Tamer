    private PathMatcherAndGroupParamNames parsePath() throws RESTAnnotationException {
        final LiteralValue[] annotationValue = getAnnotation().getValue();
        if (annotationValue.length != 1) {
            throw new RESTAnnotationException(RESTAnnotationErrorCodes.RQST0001);
        }
        final AtomicValue pathValue = annotationValue[0].getValue();
        if (pathValue.getType() != Type.STRING) {
            throw new RESTAnnotationException(RESTAnnotationErrorCodes.RQST0002);
        }
        try {
            final String pathStr = pathValue.getStringValue();
            if (pathStr.isEmpty()) {
                throw new RESTAnnotationException(RESTAnnotationErrorCodes.RQST0003);
            }
            mchPath.reset(pathStr);
            if (!mchPath.matches()) {
                throw new RESTAnnotationException(RESTAnnotationErrorCodes.RQST0004);
            }
            final StringBuilder thisPathExprRegExp = new StringBuilder();
            final List<String> pathFnParams = new ArrayList<String>();
            mchPathSegment.reset(pathStr);
            int segmentCount = 0;
            final Map<Integer, String> groupParamNames = new HashMap<Integer, String>();
            int groupCount = 0;
            while (mchPathSegment.find()) {
                final String pathSegment = pathStr.substring(mchPathSegment.start(), mchPathSegment.end());
                mtcFnParameter.reset(pathSegment);
                thisPathExprRegExp.append(URI_PATH_SEGMENT_DELIMITER);
                if (mtcFnParameter.matches()) {
                    final String fnParamName = mtcFnParameter.replaceFirst("$1");
                    pathFnParams.add(fnParamName);
                    thisPathExprRegExp.append("(");
                    thisPathExprRegExp.append(ncPcharRegExp);
                    thisPathExprRegExp.append(")");
                    groupParamNames.put(++groupCount, fnParamName);
                } else {
                    thisPathExprRegExp.append("(?:");
                    thisPathExprRegExp.append(Pattern.quote(pathSegment));
                    thisPathExprRegExp.append(")");
                }
                segmentCount++;
            }
            setPathSegmentCount(segmentCount);
            checkFnDeclaresParameters(getAnnotation().getFunctionSignature(), pathFnParams);
            final Pattern ptnThisPath = Pattern.compile(thisPathExprRegExp.toString());
            final Matcher mtcThisPath = ptnThisPath.matcher("");
            return new PathMatcherAndGroupParamNames(mtcThisPath, groupParamNames);
        } catch (XPathException xpe) {
            throw new RESTAnnotationException(RESTAnnotationErrorCodes.RQST0004, xpe);
        }
    }

    @Override
    public final String[] getDirectURL(HttpClient client, String referenceURL) throws IOException {
        String responseString;
        HttpResponse response = client.execute(new HttpGet("http://www.mediafire.com/"));
        response.getEntity().consumeContent();
        response = client.execute(new HttpGet(referenceURL));
        responseString = EntityUtils.toString(response.getEntity());
        if (DEBUG) System.out.println(responseString);
        String unescape = "unescape".intern();
        int index = responseString.indexOf(unescape);
        if (index < 0) return null;
        String t1 = responseString.substring(0, index);
        responseString = responseString.substring(t1.lastIndexOf("();") + 3);
        index = responseString.indexOf("eval(");
        t1 = responseString.substring(index);
        if (responseString.charAt(index + 5) == '\"') index = t1.indexOf("\");") + 4; else index = t1.indexOf(';') + 1;
        responseString = responseString.substring(0, responseString.indexOf("eval") + index);
        try {
            if (DEBUG) System.out.println(responseString);
        } catch (Exception any) {
            if (DEBUG) any.printStackTrace(System.out);
        }
        String functionToAddName = null;
        String javascript = responseString;
        responseString = null;
        Object result = null;
        try {
            result = context.evaluateString(scope, javascript, "", 0, null);
        } catch (Exception any) {
            try {
                functionToAddName = any.getMessage().substring(any.getMessage().indexOf('\"') + 1);
                functionToAddName = functionToAddName.substring(0, functionToAddName.lastIndexOf('\"'));
            } catch (Exception nay) {
                functionToAddName = null;
                if (DEBUG) System.out.println("could not find func");
            }
        }
        FunctionObject mediafireFakeFunction = null;
        if (functionToAddName != null) {
            try {
                mediafireFakeFunction = new FunctionObject(functionToAddName, MediafireDirectLinkProvider.class.getMethod("mediafireFakeFunction", Object.class, Object.class, Object.class), scope);
                ScriptableObject.defineProperty(scope, functionToAddName, mediafireFakeFunction, 0);
            } catch (NoSuchMethodException exception) {
                if (DEBUG) exception.printStackTrace();
            }
        }
        try {
            result = context.evaluateString(scope, javascript, "", 0, null);
        } catch (Exception any) {
            if (DEBUG) any.printStackTrace(System.out);
            try {
                functionToAddName = any.getMessage().substring(any.getMessage().indexOf('\"') + 1);
                functionToAddName = functionToAddName.substring(0, functionToAddName.lastIndexOf('\"'));
            } catch (Exception an) {
                if (DEBUG) an.printStackTrace();
            }
            result = Context.toString(result);
            Context.exit();
        }
        String[] nextPageParams = result.toString().split(" ");
        if (DEBUG) System.out.println("Variables qk,pk,r=");
        for (int i = 0; i < nextPageParams.length; i++) {
            if (DEBUG) System.out.println(nextPageParams[i]);
        }
        if (nextPageParams.length < 3) {
            if (DEBUG) System.out.println("could not find qk,pk and r");
            return null;
        }
        response = client.execute(new HttpGet("http://www.mediafire.com/dynamic/download.php?" + "qk=" + nextPageParams[0] + "&pk=" + nextPageParams[1] + "&r=" + nextPageParams[2]));
        qk = nextPageParams[0];
        responseString = EntityUtils.toString(response.getEntity());
        if (DEBUG) System.out.println(responseString);
        if (DEBUG) System.out.println("looking for name");
        if (fileName == null) {
            String pattern = "/autodisable.php/";
            index = responseString.indexOf(pattern);
            if (index > 0) {
                try {
                    fileName = responseString.substring(index + pattern.length());
                    fileName = fileName.substring(0, fileName.indexOf("\">"));
                } catch (Exception any) {
                    fileName = null;
                }
            } else {
                if (DEBUG) System.out.println("could not file file name");
            }
        }
        try {
            customEvaluationFunction = new EvalFilterFunctionObject(this, customEvaluationFunctionName, scope);
            ScriptableObject.defineProperty(scope, customEvaluationFunctionName, customEvaluationFunction, 0);
        } catch (Exception exception) {
            if (DEBUG) exception.printStackTrace();
        }
        String downloadKeyFinder = "Please report this key to support (";
        index = responseString.indexOf(downloadKeyFinder);
        downloadKey = responseString.substring(index + downloadKeyFinder.length());
        downloadKey = downloadKey.substring(0, downloadKey.indexOf(')'));
        String vars = "\"Javascript\"><!--";
        vars = responseString.substring(responseString.indexOf("\"Javascript\"><!--") + vars.length());
        vars = vars.substring(0, vars.indexOf("function"));
        responseString = responseString.substring(responseString.indexOf("case 15:") + 8);
        responseString = responseString.substring(0, responseString.indexOf("break;"));
        System.out.println("++++++++++With parent entires++++++++");
        System.out.println(responseString);
        System.out.println("----------With parent entires--------");
        t1 = "parent";
        String p1, p2;
        for (index = responseString.indexOf(t1); index > 0; ) {
            p1 = responseString.substring(0, index);
            p2 = responseString.substring(index);
            p2 = " null" + p2.substring(p2.indexOf(')') + 1);
            responseString = p1 + p2;
            index = responseString.indexOf(t1);
        }
        System.out.println("++++++++++Without parent entires++++++++");
        System.out.println(responseString);
        System.out.println("----------Without parent entires--------");
        responseString = responseString.replace("eval", customEvaluationFunctionName);
        responseString = vars + responseString;
        if (DEBUG) System.out.println("++++++++++Executing+++++++++");
        if (DEBUG) System.out.println(responseString);
        if (DEBUG) System.out.println("++++++++++Executing+++++++++");
        try {
            Object o = context.evaluateString(scope, responseString, "<Link last step>", 0, null);
            if (DEBUG) System.out.println(o);
            if (DEBUG) System.out.println(context.toString(o));
        } catch (Exception any) {
            if (DEBUG) any.printStackTrace(System.out);
        }
        Context.exit();
        if (mediafireserver == null) {
            System.out.println("could not find mediafireservernumber");
            return null;
        }
        return new String[] { "http://" + mediafireserver + ".mediafire.com/" + downloadKey + "g/" + qk + "/", fileName };
    }

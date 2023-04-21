    public BrushGroovy() {
        super();
        String keywords = "as assert break case catch class continue def default do else extends finally " + "if in implements import instanceof interface new package property return switch " + "throw throws try while public protected private static";
        String types = "void boolean byte char short int long float double";
        String constants = "null";
        String methods = "allProperties count get size " + "collect each eachProperty eachPropertyName eachWithIndex find findAll " + "findIndexOf grep inject max min reverseEach sort " + "asImmutable asSynchronized flatten intersect join pop reverse subMap toList " + "padRight padLeft contains eachMatch toCharacter toLong toUrl tokenize " + "eachFile eachFileRecurse eachB yte eachLine readBytes readLine getText " + "splitEachLine withReader append encodeBase64 decodeBase64 filterLine " + "transformChar transformLine withOutputStream withPrintWriter withStream " + "withStreams withWriter withWriterAppend write writeLine " + "dump inspect invokeMethod print println step times upto use waitForOrKill " + "getText";
        List<RegExpRule> _regExpRuleList = new ArrayList<RegExpRule>();
        _regExpRuleList.add(new RegExpRule(RegExpRule.singleLineCComments, "comments"));
        _regExpRuleList.add(new RegExpRule(RegExpRule.multiLineCComments, "comments"));
        _regExpRuleList.add(new RegExpRule(RegExpRule.doubleQuotedString, "string"));
        _regExpRuleList.add(new RegExpRule(RegExpRule.singleQuotedString, "string"));
        _regExpRuleList.add(new RegExpRule("\"\"\".*\"\"\"", "string"));
        _regExpRuleList.add(new RegExpRule("\\b([\\d]+(\\.[\\d]+)?|0x[a-f0-9]+)\\b", Pattern.CASE_INSENSITIVE, "value"));
        _regExpRuleList.add(new RegExpRule(getKeywords(keywords), Pattern.MULTILINE, "keyword"));
        _regExpRuleList.add(new RegExpRule(getKeywords(types), Pattern.MULTILINE, "color1"));
        _regExpRuleList.add(new RegExpRule(getKeywords(constants), Pattern.MULTILINE, "constants"));
        _regExpRuleList.add(new RegExpRule(getKeywords(methods), Pattern.MULTILINE, "functions"));
        setRegExpRuleList(_regExpRuleList);
        setHTMLScriptRegExp(HTMLScriptRegExp.aspScriptTags);
        setCommonFileExtensionList(Arrays.asList("groovy"));
    }

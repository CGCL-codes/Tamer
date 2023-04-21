    public BrushDelphi() {
        super();
        String keywords = "abs addr and ansichar ansistring array as asm begin boolean byte cardinal " + "case char class comp const constructor currency destructor div do double " + "downto else end except exports extended false file finalization finally " + "for function goto if implementation in inherited int64 initialization " + "integer interface is label library longint longword mod nil not object " + "of on or packed pansichar pansistring pchar pcurrency pdatetime pextended " + "pint64 pointer private procedure program property pshortstring pstring " + "pvariant pwidechar pwidestring protected public published raise real real48 " + "record repeat set shl shortint shortstring shr single smallint string then " + "threadvar to true try type unit until uses val var varirnt while widechar " + "widestring with word write writeln xor";
        List<RegExpRule> _regExpRuleList = new ArrayList<RegExpRule>();
        _regExpRuleList.add(new RegExpRule("\\(\\*[\\s\\S]*?\\*\\)", Pattern.MULTILINE, "comments"));
        _regExpRuleList.add(new RegExpRule("\\{(?!\\$)[\\s\\S]*?\\}", Pattern.MULTILINE, "comments"));
        _regExpRuleList.add(new RegExpRule(RegExpRule.singleLineCComments, "comments"));
        _regExpRuleList.add(new RegExpRule(RegExpRule.singleQuotedString, "string"));
        _regExpRuleList.add(new RegExpRule("\\{\\$[a-zA-Z]+ .+\\}", "color1"));
        _regExpRuleList.add(new RegExpRule("\\b[\\d\\.]+\\b", "value"));
        _regExpRuleList.add(new RegExpRule("\\$[a-zA-Z0-9]+\\b", "value"));
        _regExpRuleList.add(new RegExpRule(getKeywords(keywords), Pattern.MULTILINE | Pattern.CASE_INSENSITIVE, "keyword"));
        setRegExpRuleList(_regExpRuleList);
        setCommonFileExtensionList(Arrays.asList("pas"));
    }

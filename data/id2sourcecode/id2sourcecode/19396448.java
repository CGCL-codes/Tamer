    protected String protectText(String content, String markupRegex) {
        Matcher matcher = Pattern.compile(markupRegex, Pattern.MULTILINE | Pattern.DOTALL).matcher(content);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String protectedText = matcher.group();
            String hash = DigesterUtil.digest(protectedText);
            matcher.appendReplacement(sb, "$1" + hash + "$3");
            _protectedMap.put(hash, matcher.group(2));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

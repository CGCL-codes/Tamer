    @Override
    protected Document doWork(Document document) throws OperatorException {
        String text = document.getText();
        Matcher matcher = null;
        if (getParameterAsBoolean(PARAMETER_OVERRIDE_CONTENT_TYPE_INFORMATION)) {
            String previousEncoding = URLConnectionProvider.DEFAULT_ENCODING;
            if (document.getMetaDataKeys().contains(GetWebpageOperator.META_DATA_CONTENT_TYPE)) {
                previousEncoding = URLConnectionProvider.parseEncoding((String) document.getMetaDataValue(GetWebpageOperator.META_DATA_CONTENT_TYPE));
            }
            matcher = Pattern.compile("<meta[\\s]*http-equiv=[\"]*([^>]*?)[\"]*[\\s]*content=[\"]*([^>]*?)[\"]*[\\s]*?[/]*?>").matcher(text.toLowerCase());
            while (matcher.find()) {
                if (matcher.groupCount() >= 2) {
                    String key = matcher.group(1).trim();
                    String value = matcher.group(2).trim();
                    if (key.toLowerCase().equals("content-type")) {
                        String encoding = URLConnectionProvider.parseEncoding(value.replace("\"", ""));
                        try {
                            text = new String(text.getBytes(previousEncoding), Encoding.getEncoding(encoding));
                            encodingChanged = true;
                        } catch (UnsupportedEncodingException e) {
                            text = new String(text.getBytes(), Encoding.getEncoding(encoding));
                        } catch (IllegalCharsetNameException e) {
                        } catch (UnsupportedCharsetException e) {
                        }
                        document.addMetaData(GetWebpageOperator.META_DATA_CONTENT_TYPE, value, Ontology.NOMINAL);
                        break;
                    }
                }
            }
        }
        int titleStart = text.toLowerCase().indexOf("<title>") + "<title>".length();
        int titleEnd = text.toLowerCase().indexOf("</title>");
        if (titleStart >= 0 && titleEnd >= 0 && titleStart < titleEnd) {
            String title = text.substring(titleStart, titleEnd).trim();
            if (title != null && !title.isEmpty()) {
                document.addMetaData(META_DATA_HTML_TITLE, StringEscapeUtils.unescapeHtml(title), Ontology.NOMINAL);
            } else {
                document.addMetaData(META_DATA_HTML_TITLE, (String) null, Ontology.NOMINAL);
            }
        } else {
            document.addMetaData(META_DATA_HTML_TITLE, (String) null, Ontology.NOMINAL);
        }
        document.addMetaData(META_DATA_HTML_LANGUAGE, (String) null, Ontology.NOMINAL);
        document.addMetaData(META_DATA_HTML_DESCRIPTION, (String) null, Ontology.NOMINAL);
        document.addMetaData(META_DATA_HTML_KEYWORDS, (String) null, Ontology.NOMINAL);
        document.addMetaData(META_DATA_HTML_ROBOTS, (String) null, Ontology.NOMINAL);
        matcher = Pattern.compile("<(meta|META)[\\s]*(HTTP-EQUIV|http-equiv|NAME|name)=[\"]*([^>]*?)[\"]*[\\s]*(content|CONTENT)=[\"]*([^>]*?)[\"]*[\\s]*?[/]*?>").matcher(text);
        while (matcher.find()) {
            if (matcher.groupCount() >= 5) {
                String key = matcher.group(3).trim();
                String value = matcher.group(5).trim();
                if (key != null && !key.isEmpty()) {
                    if (key.toLowerCase().equals("language")) {
                        document.addMetaData(META_DATA_HTML_LANGUAGE, value, Ontology.NOMINAL);
                        continue;
                    }
                    if (key.toLowerCase().equals("description")) {
                        document.addMetaData(META_DATA_HTML_DESCRIPTION, StringEscapeUtils.unescapeHtml(value), Ontology.NOMINAL);
                        continue;
                    }
                    if (key.toLowerCase().equals("keywords")) {
                        document.addMetaData(META_DATA_HTML_KEYWORDS, StringEscapeUtils.unescapeHtml(value), Ontology.NOMINAL);
                        continue;
                    }
                    if (key.toLowerCase().equals("robots")) {
                        document.addMetaData(META_DATA_HTML_ROBOTS, value, Ontology.NOMINAL);
                        continue;
                    }
                }
            }
        }
        if (getParameterAsBoolean(PARAMETER_EXTRACT_CONTENT)) {
            text = text.replaceAll("<!--[\\s\\S]?-->", "");
            text = text.replaceAll("<style.*?>[\\s\\S]*?</style>", "");
            text = text.replaceAll("<script.*?>[\\s\\S]*?</script>", "");
            text = text.replaceAll("<img[^>]*?>", "");
            text = text.replaceAll("<a[^>]*?>(.*?)<[\\s]*/a>", " $1 ");
            if (getParameterAsBoolean(PARAMETER_NEGLEGT_SPAN_TAGS)) {
                text = text.replaceAll("<[/]*[span|SPAN][^>]*?>(.*?)<[\\s]*/span>", " $1 ");
            }
            if (getParameterAsBoolean(PARAMETER_NEGLECT_P_TAGS)) {
                text = text.replaceAll("<[/]*[p|P][^a-zA-Z>]*?>", " ");
            }
            if (getParameterAsBoolean(PARAMETER_NEGLECT_B_TAGS)) {
                text = text.replaceAll("<[/]*[b|B][^a-zA-Z>]*?>", " ");
            }
            if (getParameterAsBoolean(PARAMETER_NEGLECT_I_TAGS)) {
                text = text.replaceAll("<[/]*[i|I][^a-zA-Z>]*?>", " ");
            }
            if (getParameterAsBoolean(PARAMETER_NEGLECT_BR_TAGS)) {
                text = text.replaceAll("<(br|BR)[^>]*?>", " ");
            }
            if (getParameterAsBoolean(PARAMETER_IGNORE_NON_HTML_TAGS)) {
                matcher = Pattern.compile("</?([A-Za-z]*)?([^>]*?)>").matcher(text);
                while (matcher.find()) {
                    if (matcher.groupCount() > 0) {
                        if (!knownTags.contains(matcher.group(1).toLowerCase())) {
                            text = text.replace(matcher.group(0), "");
                        }
                    }
                }
            }
            text = text.replaceAll("(<[^>]*?>)", " <tag> ");
            text = text.replaceAll("[\r]*[\n]+", " ");
            text = text.replaceAll("[\\s]+", " ");
            String[] words = text.split("\\s");
            int[] lengths = new int[words.length];
            for (int i = 0; i < words.length; i++) {
                if (words[i].equals("<tag>")) {
                    lengths[i] = 0;
                } else {
                    lengths[i] = (i > 0 ? lengths[i - 1] : 0) + 1;
                }
            }
            for (int i = words.length - 2; i >= 0; i--) {
                if (lengths[i] != 0 && lengths[i] < lengths[i + 1]) {
                    lengths[i] = lengths[i + 1];
                }
            }
            int minLength = getParameterAsInt(PARAMETER_MIN_LENGTH);
            List<Token> tokens = new LinkedList<Token>();
            for (int i = 0; i < words.length; i++) {
                if (lengths[i] >= minLength) {
                    StringBuffer buf = new StringBuffer();
                    for (int j = 0; j < lengths[i]; j++) {
                        buf.append(words[i++] + " ");
                        if (i >= lengths.length) {
                            break;
                        }
                    }
                    tokens.add(new Token(StringEscapeUtils.unescapeHtml(buf.toString().trim()), 1));
                }
            }
            return new Document(tokens, new Document(Collections.singletonList(new Token(text, 1f)), document));
        } else {
            if (encodingChanged) {
                Document newDocument = new Document(Collections.singletonList(new Token(text, 1f)), new Document(Collections.singletonList(new Token(text, 1f)), document));
                newDocument.getText();
                return newDocument;
            } else {
                return document;
            }
        }
    }

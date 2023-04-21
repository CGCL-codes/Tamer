    @SuppressWarnings("nls")
    @Override
    public Map<String, EosDocument> toSentenceDocuments(final EosDocument doc, final SentenceTokenizer sentencer, final ResettableTokenizer tokenizer, final TextBuilder builder) throws EosException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("SentenceTokenizer instance: " + sentencer.getClass());
            LOG.debug("ResettableTokenizer instance: " + tokenizer.getClass());
            LOG.debug("TextBuilder instance: " + builder.getClass());
        }
        final Map<String, EosDocument> retval = new HashMap<String, EosDocument>();
        final MessageDigest md = createDigester();
        final Map<String, List<String>> meta = doc.getMeta();
        final CharSequence newTitle = extractTitle(doc, tokenizer, builder);
        final List<CharSequence> sentences = extractSentences(doc, sentencer, tokenizer, builder);
        for (final CharSequence newText : sentences) {
            final EosDocument newDoc = new EosDocument();
            newDoc.setText(newText);
            newDoc.setTitle(newTitle);
            final Map<String, List<String>> newMeta = newDoc.getMeta();
            newMeta.putAll(meta);
            try {
                final byte[] bytes = ("" + newText).getBytes("UTF-8");
                md.reset();
                final byte[] key = md.digest(bytes);
                final char[] asChar = Hex.encodeHex(key);
                final String asString = new String(asChar);
                retval.put(asString, newDoc);
            } catch (final UnsupportedEncodingException e) {
                throw new TokenizerException(e);
            }
        }
        return retval;
    }

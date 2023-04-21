    public byte[] calculate(Content content, Parse parse) {
        int MIN_TOKEN_LEN = getConf().getInt("db.signature.text_profile.min_token_len", 2);
        float QUANT_RATE = getConf().getFloat("db.signature.text_profile.quant_rate", 0.01f);
        HashMap tokens = new HashMap();
        String text = null;
        if (parse != null) text = parse.getText();
        if (text == null || text.length() == 0) return fallback.calculate(content, parse);
        StringBuffer curToken = new StringBuffer();
        int maxFreq = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                curToken.append(Character.toLowerCase(c));
            } else {
                if (curToken.length() > 0) {
                    if (curToken.length() > MIN_TOKEN_LEN) {
                        String s = curToken.toString();
                        Token tok = (Token) tokens.get(s);
                        if (tok == null) {
                            tok = new Token(0, s);
                            tokens.put(s, tok);
                        }
                        tok.cnt++;
                        if (tok.cnt > maxFreq) maxFreq = tok.cnt;
                    }
                    curToken.setLength(0);
                }
            }
        }
        if (curToken.length() > MIN_TOKEN_LEN) {
            String s = curToken.toString();
            Token tok = (Token) tokens.get(s);
            if (tok == null) {
                tok = new Token(0, s);
                tokens.put(s, tok);
            }
            tok.cnt++;
            if (tok.cnt > maxFreq) maxFreq = tok.cnt;
        }
        Iterator it = tokens.values().iterator();
        ArrayList profile = new ArrayList();
        int QUANT = Math.round(maxFreq * QUANT_RATE);
        if (QUANT < 2) {
            if (maxFreq > 1) QUANT = 2; else QUANT = 1;
        }
        while (it.hasNext()) {
            Token t = (Token) it.next();
            t.cnt = (t.cnt / QUANT) * QUANT;
            if (t.cnt < QUANT) {
                continue;
            }
            profile.add(t);
        }
        Collections.sort(profile, new TokenComparator());
        StringBuffer newText = new StringBuffer();
        it = profile.iterator();
        while (it.hasNext()) {
            Token t = (Token) it.next();
            if (newText.length() > 0) newText.append("\n");
            newText.append(t.toString());
        }
        return MD5Hash.digest(newText.toString()).getDigest();
    }

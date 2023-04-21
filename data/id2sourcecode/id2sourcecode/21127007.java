    public void applyPattern(byte[] pattern) {
        if (pattern == null || pattern.length < 1) throw new IllegalArgumentException("a pattern must be specified");
        HashMap formats = new HashMap();
        List fields = new LinkedList();
        final int NIL = '\0';
        final int CURLY_BRACE_OPEN = '{';
        final int CURLY_BRACE_CLOSE = '}';
        final int SINGLE_QUOTE = '\'';
        final int UNDEF = -1;
        int curly_start = UNDEF;
        int quote_start = UNDEF;
        int pos = 0;
        int end = pos + pattern.length - 1;
        do {
            switch(pattern[pos]) {
                case CURLY_BRACE_OPEN:
                    if (quote_start == UNDEF) {
                        if (curly_start != UNDEF) throw new IllegalArgumentException("nesting curly braces not supported yet, pos=" + pos);
                        curly_start = pos;
                    }
                    break;
                case CURLY_BRACE_CLOSE:
                    if (quote_start == UNDEF) {
                        if (curly_start == UNDEF) throw new IllegalArgumentException("missing opening curly brace, pos=" + pos);
                        createFormatter(pattern, curly_start, pos, formats, fields);
                        curly_start = UNDEF;
                    }
                    break;
                case SINGLE_QUOTE:
                    if (curly_start != UNDEF) {
                    } else if (quote_start == UNDEF) {
                        quote_start = pos;
                    } else {
                        int diff = pos - quote_start;
                        if (diff > 1) {
                            for (int i = quote_start; i < pos - 1; i++) pattern[i] = pattern[i + 1];
                            pos--;
                            for (int i = pos; i < end - 1; i++) pattern[i] = pattern[i + 2];
                            pos--;
                            end -= 2;
                        } else {
                            for (int i = pos; i < end; i++) pattern[i] = pattern[i + 1];
                            pos--;
                            end--;
                        }
                        quote_start = UNDEF;
                    }
                    break;
            }
        } while (++pos <= end);
        if (curly_start != UNDEF) throw new IllegalArgumentException("unmatched braces in the pattern");
        if (quote_start != UNDEF) throw new IllegalArgumentException("unmatched single quotes in the pattern");
        clean_pattern = new byte[end + 1];
        System.arraycopy(pattern, 0, clean_pattern, 0, clean_pattern.length);
        int max_index = -1;
        Iterator it = formats.keySet().iterator();
        while (it.hasNext()) {
            Integer key = (Integer) it.next();
            if (key.intValue() > max_index) max_index = key.intValue();
        }
        if (max_index >= 0) {
            xformats = new FormatB[max_index + 1];
            it = formats.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                Integer key = (Integer) entry.getKey();
                xformats[key.intValue()] = (FormatB) formats.get(key);
            }
            int index = 0;
            xfields = new FieldPosition[fields.size()];
            it = fields.iterator();
            while (it.hasNext()) xfields[index++] = (FieldPosition) it.next();
        }
    }

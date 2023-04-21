    public void setPermissions(String str) throws SyntaxException {
        StringTokenizer tokenizer = new StringTokenizer(str, ",= ");
        String token;
        int shift = -1;
        while (tokenizer.hasMoreTokens()) {
            token = tokenizer.nextToken();
            if (token.equalsIgnoreCase(USER_STRING)) shift = 6; else if (token.equalsIgnoreCase(GROUP_STRING)) shift = 3; else if (token.equalsIgnoreCase(OTHER_STRING)) shift = 0; else {
                char modifier = token.charAt(0);
                if (!(modifier == '+' || modifier == '-')) throw new SyntaxException("expected modifier +|-"); else token = token.substring(1);
                if (token.length() == 0) throw new SyntaxException("'read', 'write' or 'update' " + "expected in permission string");
                int perm;
                if (token.equalsIgnoreCase("read")) perm = READ; else if (token.equalsIgnoreCase("write")) perm = WRITE; else perm = UPDATE;
                switch(modifier) {
                    case '+':
                        permissions = permissions | (perm << shift);
                        break;
                    default:
                        permissions = permissions & (~(perm << shift));
                        break;
                }
            }
        }
    }

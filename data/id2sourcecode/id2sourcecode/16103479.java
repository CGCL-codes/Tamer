    public IDLValue parseIDLArrayTokens(String[] tokens, int type) {
        IDLValue expr1 = null, result = null;
        int ileft;
        int iright;
        if (tokens.length == 0) {
            return null;
        }
        if (tokens.length == 1) {
            expr1 = new IDLValue(Double.parseDouble(tokens[0]));
            return expr1;
        }
        String[] ops = null;
        int next_parser;
        if (type == EXPR) {
            ops = new String[2];
            ops[0] = "+";
            ops[1] = "-";
            next_parser = TERM;
        } else if (type == TERM) {
            ops = new String[2];
            ops[0] = "*";
            ops[1] = "/";
            next_parser = FACTOR;
        } else if (type == FACTOR) {
            ops = new String[1];
            ops[0] = "^";
            next_parser = NUMBER;
            if (tokens[0].equals("-")) {
                String[] expr = new String[tokens.length - 1];
                for (int i = 0; i < expr.length; i++) expr[i] = tokens[i + 1];
                expr1 = parseIDLArrayTokens(expr, FACTOR);
                result = expr1.IDLmultiply(new IDLValue(-1));
                return result;
            }
        } else {
            next_parser = NOPARSER;
            if (tokens[0].equals("(") && tokens[tokens.length - 1].equals(")")) {
                ileft = 1;
                iright = tokens.length - 2;
                String[] expr = new String[iright - ileft + 1];
                for (int i = 0; i < expr.length; i++) expr[i] = tokens[i + ileft];
                expr1 = parseIDLArrayTokens(expr, EXPR);
                return expr1;
            } else if (tokens[0].equals("[") && tokens[tokens.length - 1].equals("]")) {
                expr1 = parseIDLExprList(tokens);
                return expr1;
            } else if (tokens[0].equalsIgnoreCase("findgen") || tokens[0].equalsIgnoreCase("dindgen")) {
                String[] expr = new String[tokens.length - 3];
                for (int i = 0; i < expr.length; i++) expr[i] = tokens[i + 2];
                expr1 = parseIDLArrayTokens(expr, EXPR);
                if (expr1.type != IDLValue.SCALAR) {
                    org.das2.util.DasDie.println("Syntax error in findgen argument");
                    System.exit(-1);
                } else {
                    expr1 = IDLValue.findgen((int) expr1.sValue);
                }
            } else if (tokens[0].equalsIgnoreCase("alog10")) {
                String[] expr = new String[tokens.length - 3];
                for (int i = 0; i < expr.length; i++) expr[i] = tokens[i + 2];
                expr1 = parseIDLArrayTokens(expr, EXPR);
                expr1 = IDLValue.alog10(expr1);
            } else if (tokens[0].equalsIgnoreCase("sin")) {
                String[] expr = new String[tokens.length - 3];
                for (int i = 0; i < expr.length; i++) expr[i] = tokens[i + 2];
                expr1 = parseIDLArrayTokens(expr, EXPR);
                expr1 = IDLValue.sin(expr1);
            } else {
                return null;
            }
            return expr1;
        }
        int ileftop = 9999;
        String leftop = "";
        int nleft_paren = 0;
        int nleft_bracket = 0;
        for (int iop = 0; iop < ops.length; iop++) {
            for (int itok = 0; itok < tokens.length; itok++) {
                if (tokens[itok].equals("[")) nleft_bracket++;
                if (tokens[itok].equals("(")) nleft_paren++;
                if (tokens[itok].equals("]")) nleft_bracket--;
                if (tokens[itok].equals(")")) nleft_paren--;
                if (tokens[itok].equals(ops[iop]) && itok != 0) {
                    if ((iop < ileftop) && (nleft_paren == 0) && (nleft_bracket == 0)) {
                        ileftop = itok;
                        leftop = ops[iop];
                    }
                }
            }
        }
        if (ileftop == 9999) {
            result = parseIDLArrayTokens(tokens, next_parser);
        } else {
            String[] expr = new String[ileftop];
            for (int i = 0; i < ileftop; i++) expr[i] = tokens[i];
            expr1 = parseIDLArrayTokens(expr, next_parser);
            expr = new String[tokens.length - ileftop - 1];
            for (int i = 0; i < expr.length; i++) expr[i] = tokens[i + ileftop + 1];
            IDLValue expr2 = parseIDLArrayTokens(expr, type);
            if (expr1 == null || expr2 == null) {
                if (next_parser == NOPARSER) result = null; else result = parseIDLArrayTokens(tokens, next_parser);
            } else if (leftop.equals("+")) {
                result = expr1.IDLadd(expr2);
            } else if (leftop.equals("-")) {
                result = expr1.IDLsubtract(expr2);
            } else if (leftop.equals("*")) {
                result = expr1.IDLmultiply(expr2);
            } else if (leftop.equals("/")) {
                result = expr1.IDLdivide(expr2);
            } else if (leftop.equals("^")) {
                result = expr1.IDLexponeniate(expr2);
            }
        }
        return result;
    }

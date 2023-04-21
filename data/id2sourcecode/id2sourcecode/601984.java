    public static void main(String[] args) {
        try {
            ANTLRInputStream in = new ANTLRInputStream(System.in);
            FireLexer lexer = new FireLexer(in);
            WSFilter filter = new WSFilter(lexer);
            CommonTokenStream tokens = new CommonTokenStream(filter);
            tokens.discardOffChannelTokens(false);
            for (Object o : tokens.getTokens()) {
                Token t = (Token) o;
                System.out.print("<" + FireParser.tokenNames[t.getType()] + "," + escape(t.getText()) + "," + t.getChannel() + "> ");
                if (t.getType() == FireLexer.NEWLINE) System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

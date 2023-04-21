    private ArrayList<Token> createPackedList() {
        int i = 0;
        Token tk = null;
        ArrayList<Token> pList = new ArrayList<Token>(this.lineLength + 1);
        for (i = 0; i < currLine.size(); i++) {
            tk = getTokenFromCurrLine(i);
            try {
                if (tk.getChannel() != lexer.getIgnoreChannelNumber()) {
                    pList.add(tk);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        if (pList.get(pList.size() - 1).getType() != FortranLexer.T_EOS) {
            FortranToken eos = new FortranToken(lexer.getInput(), FortranLexer.T_EOS, Token.DEFAULT_CHANNEL, lexer.getInput().index(), lexer.getInput().index() + 1);
            eos.setText("\n");
            packedList.add(eos);
        }
        return pList;
    }

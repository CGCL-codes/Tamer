    @SuppressWarnings("unchecked")
    public Start parse() throws ParserException, LexerException, IOException {
        push(0, null);
        List<Node> ign = null;
        while (true) {
            while (index(this.lexer.peek()) == -1) {
                if (ign == null) {
                    ign = new LinkedList<Node>();
                }
                ign.add(this.lexer.next());
            }
            if (ign != null) {
                this.ignoredTokens.setIn(this.lexer.peek(), ign);
                ign = null;
            }
            this.last_pos = this.lexer.peek().getPos();
            this.last_line = this.lexer.peek().getLine();
            this.last_token = this.lexer.peek();
            int index = index(this.lexer.peek());
            this.action[0] = Parser.actionTable[state()][0][1];
            this.action[1] = Parser.actionTable[state()][0][2];
            int low = 1;
            int high = Parser.actionTable[state()].length - 1;
            while (low <= high) {
                int middle = (low + high) / 2;
                if (index < Parser.actionTable[state()][middle][0]) {
                    high = middle - 1;
                } else if (index > Parser.actionTable[state()][middle][0]) {
                    low = middle + 1;
                } else {
                    this.action[0] = Parser.actionTable[state()][middle][1];
                    this.action[1] = Parser.actionTable[state()][middle][2];
                    break;
                }
            }
            switch(this.action[0]) {
                case SHIFT:
                    {
                        ArrayList list = new ArrayList();
                        list.add(this.lexer.next());
                        push(this.action[1], list);
                    }
                    break;
                case REDUCE:
                    switch(this.action[1]) {
                        case 0:
                            {
                                ArrayList list = new0();
                                push(goTo(0), list);
                            }
                            break;
                        case 1:
                            {
                                ArrayList list = new1();
                                push(goTo(1), list);
                            }
                            break;
                        case 2:
                            {
                                ArrayList list = new2();
                                push(goTo(1), list);
                            }
                            break;
                        case 3:
                            {
                                ArrayList list = new3();
                                push(goTo(2), list);
                            }
                            break;
                        case 4:
                            {
                                ArrayList list = new4();
                                push(goTo(2), list);
                            }
                            break;
                        case 5:
                            {
                                ArrayList list = new5();
                                push(goTo(3), list);
                            }
                            break;
                        case 6:
                            {
                                ArrayList list = new6();
                                push(goTo(3), list);
                            }
                            break;
                        case 7:
                            {
                                ArrayList list = new7();
                                push(goTo(3), list);
                            }
                            break;
                        case 8:
                            {
                                ArrayList list = new8();
                                push(goTo(3), list);
                            }
                            break;
                    }
                    break;
                case ACCEPT:
                    {
                        EOF node2 = (EOF) this.lexer.next();
                        PPattern node1 = (PPattern) pop().get(0);
                        Start node = new Start(node1, node2);
                        return node;
                    }
                case ERROR:
                    throw new ParserException(this.last_token, "[" + this.last_line + "," + this.last_pos + "] " + Parser.errorMessages[Parser.errors[this.action[1]]]);
            }
        }
    }

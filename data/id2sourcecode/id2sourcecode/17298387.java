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
                                push(goTo(0), list);
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
                                push(goTo(1), list);
                            }
                            break;
                        case 4:
                            {
                                ArrayList list = new4();
                                push(goTo(1), list);
                            }
                            break;
                        case 5:
                            {
                                ArrayList list = new5();
                                push(goTo(2), list);
                            }
                            break;
                        case 6:
                            {
                                ArrayList list = new6();
                                push(goTo(2), list);
                            }
                            break;
                        case 7:
                            {
                                ArrayList list = new7();
                                push(goTo(2), list);
                            }
                            break;
                        case 8:
                            {
                                ArrayList list = new8();
                                push(goTo(2), list);
                            }
                            break;
                        case 9:
                            {
                                ArrayList list = new9();
                                push(goTo(2), list);
                            }
                            break;
                        case 10:
                            {
                                ArrayList list = new10();
                                push(goTo(2), list);
                            }
                            break;
                        case 11:
                            {
                                ArrayList list = new11();
                                push(goTo(2), list);
                            }
                            break;
                        case 12:
                            {
                                ArrayList list = new12();
                                push(goTo(2), list);
                            }
                            break;
                        case 13:
                            {
                                ArrayList list = new13();
                                push(goTo(3), list);
                            }
                            break;
                        case 14:
                            {
                                ArrayList list = new14();
                                push(goTo(3), list);
                            }
                            break;
                        case 15:
                            {
                                ArrayList list = new15();
                                push(goTo(3), list);
                            }
                            break;
                        case 16:
                            {
                                ArrayList list = new16();
                                push(goTo(3), list);
                            }
                            break;
                        case 17:
                            {
                                ArrayList list = new17();
                                push(goTo(4), list);
                            }
                            break;
                        case 18:
                            {
                                ArrayList list = new18();
                                push(goTo(5), list);
                            }
                            break;
                        case 19:
                            {
                                ArrayList list = new19();
                                push(goTo(5), list);
                            }
                            break;
                        case 20:
                            {
                                ArrayList list = new20();
                                push(goTo(6), list);
                            }
                            break;
                        case 21:
                            {
                                ArrayList list = new21();
                                push(goTo(6), list);
                            }
                            break;
                        case 22:
                            {
                                ArrayList list = new22();
                                push(goTo(6), list);
                            }
                            break;
                        case 23:
                            {
                                ArrayList list = new23();
                                push(goTo(6), list);
                            }
                            break;
                        case 24:
                            {
                                ArrayList list = new24();
                                push(goTo(6), list);
                            }
                            break;
                        case 25:
                            {
                                ArrayList list = new25();
                                push(goTo(7), list);
                            }
                            break;
                        case 26:
                            {
                                ArrayList list = new26();
                                push(goTo(8), list);
                            }
                            break;
                        case 27:
                            {
                                ArrayList list = new27();
                                push(goTo(8), list);
                            }
                            break;
                        case 28:
                            {
                                ArrayList list = new28();
                                push(goTo(9), list);
                            }
                            break;
                        case 29:
                            {
                                ArrayList list = new29();
                                push(goTo(9), list);
                            }
                            break;
                        case 30:
                            {
                                ArrayList list = new30();
                                push(goTo(10), list);
                            }
                            break;
                        case 31:
                            {
                                ArrayList list = new31();
                                push(goTo(10), list);
                            }
                            break;
                    }
                    break;
                case ACCEPT:
                    {
                        EOF node2 = (EOF) this.lexer.next();
                        PBibtex node1 = (PBibtex) pop().get(0);
                        Start node = new Start(node1, node2);
                        return node;
                    }
                case ERROR:
                    throw new ParserException(this.last_token, "[" + this.last_line + "," + this.last_pos + "] " + Parser.errorMessages[Parser.errors[this.action[1]]]);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public Start parse() throws ParserException, LexerException, IOException {
        push(0, null, true);
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
                        push(this.action[1], list, false);
                    }
                    break;
                case REDUCE:
                    switch(this.action[1]) {
                        case 0:
                            {
                                ArrayList list = new0();
                                push(goTo(0), list, false);
                            }
                            break;
                        case 1:
                            {
                                ArrayList list = new1();
                                push(goTo(1), list, false);
                            }
                            break;
                        case 2:
                            {
                                ArrayList list = new2();
                                push(goTo(2), list, false);
                            }
                            break;
                        case 3:
                            {
                                ArrayList list = new3();
                                push(goTo(3), list, false);
                            }
                            break;
                        case 4:
                            {
                                ArrayList list = new4();
                                push(goTo(4), list, false);
                            }
                            break;
                        case 5:
                            {
                                ArrayList list = new5();
                                push(goTo(5), list, false);
                            }
                            break;
                        case 6:
                            {
                                ArrayList list = new6();
                                push(goTo(6), list, false);
                            }
                            break;
                        case 7:
                            {
                                ArrayList list = new7();
                                push(goTo(7), list, false);
                            }
                            break;
                        case 8:
                            {
                                ArrayList list = new8();
                                push(goTo(8), list, false);
                            }
                            break;
                        case 9:
                            {
                                ArrayList list = new9();
                                push(goTo(9), list, false);
                            }
                            break;
                        case 10:
                            {
                                ArrayList list = new10();
                                push(goTo(9), list, false);
                            }
                            break;
                        case 11:
                            {
                                ArrayList list = new11();
                                push(goTo(10), list, false);
                            }
                            break;
                        case 12:
                            {
                                ArrayList list = new12();
                                push(goTo(10), list, false);
                            }
                            break;
                        case 13:
                            {
                                ArrayList list = new13();
                                push(goTo(10), list, false);
                            }
                            break;
                        case 14:
                            {
                                ArrayList list = new14();
                                push(goTo(10), list, false);
                            }
                            break;
                        case 15:
                            {
                                ArrayList list = new15();
                                push(goTo(10), list, false);
                            }
                            break;
                        case 16:
                            {
                                ArrayList list = new16();
                                push(goTo(10), list, false);
                            }
                            break;
                        case 17:
                            {
                                ArrayList list = new17();
                                push(goTo(10), list, false);
                            }
                            break;
                        case 18:
                            {
                                ArrayList list = new18();
                                push(goTo(10), list, false);
                            }
                            break;
                        case 19:
                            {
                                ArrayList list = new19();
                                push(goTo(10), list, false);
                            }
                            break;
                        case 20:
                            {
                                ArrayList list = new20();
                                push(goTo(10), list, false);
                            }
                            break;
                        case 21:
                            {
                                ArrayList list = new21();
                                push(goTo(10), list, false);
                            }
                            break;
                        case 22:
                            {
                                ArrayList list = new22();
                                push(goTo(10), list, false);
                            }
                            break;
                        case 23:
                            {
                                ArrayList list = new23();
                                push(goTo(10), list, false);
                            }
                            break;
                        case 24:
                            {
                                ArrayList list = new24();
                                push(goTo(10), list, false);
                            }
                            break;
                        case 25:
                            {
                                ArrayList list = new25();
                                push(goTo(10), list, false);
                            }
                            break;
                        case 26:
                            {
                                ArrayList list = new26();
                                push(goTo(10), list, false);
                            }
                            break;
                        case 27:
                            {
                                ArrayList list = new27();
                                push(goTo(10), list, false);
                            }
                            break;
                        case 28:
                            {
                                ArrayList list = new28();
                                push(goTo(10), list, false);
                            }
                            break;
                        case 29:
                            {
                                ArrayList list = new29();
                                push(goTo(10), list, false);
                            }
                            break;
                        case 30:
                            {
                                ArrayList list = new30();
                                push(goTo(10), list, false);
                            }
                            break;
                        case 31:
                            {
                                ArrayList list = new31();
                                push(goTo(10), list, false);
                            }
                            break;
                        case 32:
                            {
                                ArrayList list = new32();
                                push(goTo(10), list, false);
                            }
                            break;
                        case 33:
                            {
                                ArrayList list = new33();
                                push(goTo(10), list, false);
                            }
                            break;
                        case 34:
                            {
                                ArrayList list = new34();
                                push(goTo(10), list, false);
                            }
                            break;
                        case 35:
                            {
                                ArrayList list = new35();
                                push(goTo(10), list, false);
                            }
                            break;
                        case 36:
                            {
                                ArrayList list = new36();
                                push(goTo(10), list, false);
                            }
                            break;
                        case 37:
                            {
                                ArrayList list = new37();
                                push(goTo(10), list, false);
                            }
                            break;
                        case 38:
                            {
                                ArrayList list = new38();
                                push(goTo(10), list, false);
                            }
                            break;
                        case 39:
                            {
                                ArrayList list = new39();
                                push(goTo(11), list, true);
                            }
                            break;
                        case 40:
                            {
                                ArrayList list = new40();
                                push(goTo(11), list, true);
                            }
                            break;
                        case 41:
                            {
                                ArrayList list = new41();
                                push(goTo(12), list, true);
                            }
                            break;
                        case 42:
                            {
                                ArrayList list = new42();
                                push(goTo(12), list, true);
                            }
                            break;
                        case 43:
                            {
                                ArrayList list = new43();
                                push(goTo(13), list, true);
                            }
                            break;
                        case 44:
                            {
                                ArrayList list = new44();
                                push(goTo(13), list, true);
                            }
                            break;
                    }
                    break;
                case ACCEPT:
                    {
                        EOF node2 = (EOF) this.lexer.next();
                        PProgram node1 = (PProgram) pop().get(0);
                        Start node = new Start(node1, node2);
                        return node;
                    }
                case ERROR:
                    throw new ParserException(this.last_token, "[" + this.last_line + "," + this.last_pos + "] " + Parser.errorMessages[Parser.errors[this.action[1]]]);
            }
        }
    }

    public Start parse() throws ParserException, LexerException, IOException {
        push(0, null, true);
        List ign = null;
        while (true) {
            while (index(lexer.peek()) == -1) {
                if (ign == null) {
                    ign = new TypedLinkedList(NodeCast.instance);
                }
                ign.add(lexer.next());
            }
            if (ign != null) {
                ignoredTokens.setIn(lexer.peek(), ign);
                ign = null;
            }
            last_pos = lexer.peek().getPos();
            last_line = lexer.peek().getLine();
            last_token = lexer.peek();
            int index = index(lexer.peek());
            action[0] = actionTable[state()][0][1];
            action[1] = actionTable[state()][0][2];
            int low = 1;
            int high = actionTable[state()].length - 1;
            while (low <= high) {
                int middle = (low + high) / 2;
                if (index < actionTable[state()][middle][0]) {
                    high = middle - 1;
                } else if (index > actionTable[state()][middle][0]) {
                    low = middle + 1;
                } else {
                    action[0] = actionTable[state()][middle][1];
                    action[1] = actionTable[state()][middle][2];
                    break;
                }
            }
            switch(action[0]) {
                case SHIFT:
                    {
                        ArrayList list = new ArrayList();
                        list.add(lexer.next());
                        push(action[1], list, false);
                        last_shift = action[1];
                    }
                    break;
                case REDUCE:
                    switch(action[1]) {
                        case 0:
                            {
                                ArrayList list = new0();
                                push(goTo(0), list, false);
                            }
                            break;
                        case 1:
                            {
                                ArrayList list = new1();
                                push(goTo(0), list, false);
                            }
                            break;
                        case 2:
                            {
                                ArrayList list = new2();
                                push(goTo(0), list, false);
                            }
                            break;
                        case 3:
                            {
                                ArrayList list = new3();
                                push(goTo(0), list, false);
                            }
                            break;
                        case 4:
                            {
                                ArrayList list = new4();
                                push(goTo(0), list, false);
                            }
                            break;
                        case 5:
                            {
                                ArrayList list = new5();
                                push(goTo(0), list, false);
                            }
                            break;
                        case 6:
                            {
                                ArrayList list = new6();
                                push(goTo(0), list, false);
                            }
                            break;
                        case 7:
                            {
                                ArrayList list = new7();
                                push(goTo(0), list, false);
                            }
                            break;
                        case 8:
                            {
                                ArrayList list = new8();
                                push(goTo(0), list, false);
                            }
                            break;
                        case 9:
                            {
                                ArrayList list = new9();
                                push(goTo(0), list, false);
                            }
                            break;
                        case 10:
                            {
                                ArrayList list = new10();
                                push(goTo(0), list, false);
                            }
                            break;
                        case 11:
                            {
                                ArrayList list = new11();
                                push(goTo(0), list, false);
                            }
                            break;
                        case 12:
                            {
                                ArrayList list = new12();
                                push(goTo(0), list, false);
                            }
                            break;
                        case 13:
                            {
                                ArrayList list = new13();
                                push(goTo(0), list, false);
                            }
                            break;
                        case 14:
                            {
                                ArrayList list = new14();
                                push(goTo(0), list, false);
                            }
                            break;
                        case 15:
                            {
                                ArrayList list = new15();
                                push(goTo(0), list, false);
                            }
                            break;
                        case 16:
                            {
                                ArrayList list = new16();
                                push(goTo(1), list, false);
                            }
                            break;
                        case 17:
                            {
                                ArrayList list = new17();
                                push(goTo(2), list, false);
                            }
                            break;
                        case 18:
                            {
                                ArrayList list = new18();
                                push(goTo(3), list, false);
                            }
                            break;
                        case 19:
                            {
                                ArrayList list = new19();
                                push(goTo(4), list, false);
                            }
                            break;
                        case 20:
                            {
                                ArrayList list = new20();
                                push(goTo(5), list, false);
                            }
                            break;
                        case 21:
                            {
                                ArrayList list = new21();
                                push(goTo(5), list, false);
                            }
                            break;
                        case 22:
                            {
                                ArrayList list = new22();
                                push(goTo(5), list, false);
                            }
                            break;
                        case 23:
                            {
                                ArrayList list = new23();
                                push(goTo(5), list, false);
                            }
                            break;
                        case 24:
                            {
                                ArrayList list = new24();
                                push(goTo(5), list, false);
                            }
                            break;
                        case 25:
                            {
                                ArrayList list = new25();
                                push(goTo(5), list, false);
                            }
                            break;
                        case 26:
                            {
                                ArrayList list = new26();
                                push(goTo(5), list, false);
                            }
                            break;
                        case 27:
                            {
                                ArrayList list = new27();
                                push(goTo(5), list, false);
                            }
                            break;
                        case 28:
                            {
                                ArrayList list = new28();
                                push(goTo(6), list, false);
                            }
                            break;
                        case 29:
                            {
                                ArrayList list = new29();
                                push(goTo(7), list, false);
                            }
                            break;
                        case 30:
                            {
                                ArrayList list = new30();
                                push(goTo(8), list, false);
                            }
                            break;
                        case 31:
                            {
                                ArrayList list = new31();
                                push(goTo(9), list, false);
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
                                push(goTo(11), list, false);
                            }
                            break;
                        case 34:
                            {
                                ArrayList list = new34();
                                push(goTo(11), list, false);
                            }
                            break;
                        case 35:
                            {
                                ArrayList list = new35();
                                push(goTo(12), list, true);
                            }
                            break;
                        case 36:
                            {
                                ArrayList list = new36();
                                push(goTo(12), list, true);
                            }
                            break;
                        case 37:
                            {
                                ArrayList list = new37();
                                push(goTo(13), list, true);
                            }
                            break;
                        case 38:
                            {
                                ArrayList list = new38();
                                push(goTo(13), list, true);
                            }
                            break;
                        case 39:
                            {
                                ArrayList list = new39();
                                push(goTo(14), list, true);
                            }
                            break;
                        case 40:
                            {
                                ArrayList list = new40();
                                push(goTo(14), list, true);
                            }
                            break;
                        case 41:
                            {
                                ArrayList list = new41();
                                push(goTo(15), list, true);
                            }
                            break;
                        case 42:
                            {
                                ArrayList list = new42();
                                push(goTo(15), list, true);
                            }
                            break;
                    }
                    break;
                case ACCEPT:
                    {
                        EOF node2 = (EOF) lexer.next();
                        PStory node1 = (PStory) ((ArrayList) pop()).get(0);
                        Start node = new Start(node1, node2);
                        return node;
                    }
                case ERROR:
                    throw new ParserException(last_token, "[" + last_line + "," + last_pos + "] " + errorMessages[errors[action[1]]]);
            }
        }
    }

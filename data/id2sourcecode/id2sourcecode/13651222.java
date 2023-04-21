    public Start parse() throws ParserException, LexerException, IOException {
        push(0, null, false);
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
                    push(action[1], lexer.next(), true);
                    last_shift = action[1];
                    break;
                case REDUCE:
                    switch(action[1]) {
                        case 0:
                            {
                                Node node = new0();
                                push(goTo(0), node, true);
                            }
                            break;
                        case 1:
                            {
                                Node node = new1();
                                push(goTo(0), node, true);
                            }
                            break;
                        case 2:
                            {
                                Node node = new2();
                                push(goTo(61), node, false);
                            }
                            break;
                        case 3:
                            {
                                Node node = new3();
                                push(goTo(61), node, false);
                            }
                            break;
                        case 4:
                            {
                                Node node = new4();
                                push(goTo(1), node, true);
                            }
                            break;
                        case 5:
                            {
                                Node node = new5();
                                push(goTo(2), node, true);
                            }
                            break;
                        case 6:
                            {
                                Node node = new6();
                                push(goTo(2), node, true);
                            }
                            break;
                        case 7:
                            {
                                Node node = new7();
                                push(goTo(3), node, true);
                            }
                            break;
                        case 8:
                            {
                                Node node = new8();
                                push(goTo(3), node, true);
                            }
                            break;
                        case 9:
                            {
                                Node node = new9();
                                push(goTo(3), node, true);
                            }
                            break;
                        case 10:
                            {
                                Node node = new10();
                                push(goTo(4), node, true);
                            }
                            break;
                        case 11:
                            {
                                Node node = new11();
                                push(goTo(5), node, true);
                            }
                            break;
                        case 12:
                            {
                                Node node = new12();
                                push(goTo(5), node, true);
                            }
                            break;
                        case 13:
                            {
                                Node node = new13();
                                push(goTo(6), node, true);
                            }
                            break;
                        case 14:
                            {
                                Node node = new14();
                                push(goTo(6), node, true);
                            }
                            break;
                        case 15:
                            {
                                Node node = new15();
                                push(goTo(7), node, true);
                            }
                            break;
                        case 16:
                            {
                                Node node = new16();
                                push(goTo(7), node, true);
                            }
                            break;
                        case 17:
                            {
                                Node node = new17();
                                push(goTo(62), node, false);
                            }
                            break;
                        case 18:
                            {
                                Node node = new18();
                                push(goTo(62), node, false);
                            }
                            break;
                        case 19:
                            {
                                Node node = new19();
                                push(goTo(8), node, true);
                            }
                            break;
                        case 20:
                            {
                                Node node = new20();
                                push(goTo(9), node, true);
                            }
                            break;
                        case 21:
                            {
                                Node node = new21();
                                push(goTo(9), node, true);
                            }
                            break;
                        case 22:
                            {
                                Node node = new22();
                                push(goTo(10), node, true);
                            }
                            break;
                        case 23:
                            {
                                Node node = new23();
                                push(goTo(11), node, true);
                            }
                            break;
                        case 24:
                            {
                                Node node = new24();
                                push(goTo(11), node, true);
                            }
                            break;
                        case 25:
                            {
                                Node node = new25();
                                push(goTo(12), node, true);
                            }
                            break;
                        case 26:
                            {
                                Node node = new26();
                                push(goTo(12), node, true);
                            }
                            break;
                        case 27:
                            {
                                Node node = new27();
                                push(goTo(13), node, true);
                            }
                            break;
                        case 28:
                            {
                                Node node = new28();
                                push(goTo(14), node, true);
                            }
                            break;
                        case 29:
                            {
                                Node node = new29();
                                push(goTo(14), node, true);
                            }
                            break;
                        case 30:
                            {
                                Node node = new30();
                                push(goTo(14), node, true);
                            }
                            break;
                        case 31:
                            {
                                Node node = new31();
                                push(goTo(15), node, true);
                            }
                            break;
                        case 32:
                            {
                                Node node = new32();
                                push(goTo(15), node, true);
                            }
                            break;
                        case 33:
                            {
                                Node node = new33();
                                push(goTo(15), node, true);
                            }
                            break;
                        case 34:
                            {
                                Node node = new34();
                                push(goTo(16), node, true);
                            }
                            break;
                        case 35:
                            {
                                Node node = new35();
                                push(goTo(16), node, true);
                            }
                            break;
                        case 36:
                            {
                                Node node = new36();
                                push(goTo(17), node, true);
                            }
                            break;
                        case 37:
                            {
                                Node node = new37();
                                push(goTo(18), node, true);
                            }
                            break;
                        case 38:
                            {
                                Node node = new38();
                                push(goTo(18), node, true);
                            }
                            break;
                        case 39:
                            {
                                Node node = new39();
                                push(goTo(19), node, true);
                            }
                            break;
                        case 40:
                            {
                                Node node = new40();
                                push(goTo(20), node, true);
                            }
                            break;
                        case 41:
                            {
                                Node node = new41();
                                push(goTo(20), node, true);
                            }
                            break;
                        case 42:
                            {
                                Node node = new42();
                                push(goTo(63), node, false);
                            }
                            break;
                        case 43:
                            {
                                Node node = new43();
                                push(goTo(63), node, false);
                            }
                            break;
                        case 44:
                            {
                                Node node = new44();
                                push(goTo(21), node, true);
                            }
                            break;
                        case 45:
                            {
                                Node node = new45();
                                push(goTo(21), node, true);
                            }
                            break;
                        case 46:
                            {
                                Node node = new46();
                                push(goTo(21), node, true);
                            }
                            break;
                        case 47:
                            {
                                Node node = new47();
                                push(goTo(22), node, true);
                            }
                            break;
                        case 48:
                            {
                                Node node = new48();
                                push(goTo(23), node, true);
                            }
                            break;
                        case 49:
                            {
                                Node node = new49();
                                push(goTo(23), node, true);
                            }
                            break;
                        case 50:
                            {
                                Node node = new50();
                                push(goTo(24), node, true);
                            }
                            break;
                        case 51:
                            {
                                Node node = new51();
                                push(goTo(24), node, true);
                            }
                            break;
                        case 52:
                            {
                                Node node = new52();
                                push(goTo(64), node, false);
                            }
                            break;
                        case 53:
                            {
                                Node node = new53();
                                push(goTo(64), node, false);
                            }
                            break;
                        case 54:
                            {
                                Node node = new54();
                                push(goTo(25), node, true);
                            }
                            break;
                        case 55:
                            {
                                Node node = new55();
                                push(goTo(25), node, true);
                            }
                            break;
                        case 56:
                            {
                                Node node = new56();
                                push(goTo(25), node, true);
                            }
                            break;
                        case 57:
                            {
                                Node node = new57();
                                push(goTo(25), node, true);
                            }
                            break;
                        case 58:
                            {
                                Node node = new58();
                                push(goTo(25), node, true);
                            }
                            break;
                        case 59:
                            {
                                Node node = new59();
                                push(goTo(25), node, true);
                            }
                            break;
                        case 60:
                            {
                                Node node = new60();
                                push(goTo(25), node, true);
                            }
                            break;
                        case 61:
                            {
                                Node node = new61();
                                push(goTo(25), node, true);
                            }
                            break;
                        case 62:
                            {
                                Node node = new62();
                                push(goTo(26), node, true);
                            }
                            break;
                        case 63:
                            {
                                Node node = new63();
                                push(goTo(27), node, true);
                            }
                            break;
                        case 64:
                            {
                                Node node = new64();
                                push(goTo(28), node, true);
                            }
                            break;
                        case 65:
                            {
                                Node node = new65();
                                push(goTo(29), node, true);
                            }
                            break;
                        case 66:
                            {
                                Node node = new66();
                                push(goTo(29), node, true);
                            }
                            break;
                        case 67:
                            {
                                Node node = new67();
                                push(goTo(30), node, true);
                            }
                            break;
                        case 68:
                            {
                                Node node = new68();
                                push(goTo(31), node, true);
                            }
                            break;
                        case 69:
                            {
                                Node node = new69();
                                push(goTo(32), node, true);
                            }
                            break;
                        case 70:
                            {
                                Node node = new70();
                                push(goTo(32), node, true);
                            }
                            break;
                        case 71:
                            {
                                Node node = new71();
                                push(goTo(65), node, false);
                            }
                            break;
                        case 72:
                            {
                                Node node = new72();
                                push(goTo(65), node, false);
                            }
                            break;
                        case 73:
                            {
                                Node node = new73();
                                push(goTo(32), node, true);
                            }
                            break;
                        case 74:
                            {
                                Node node = new74();
                                push(goTo(66), node, false);
                            }
                            break;
                        case 75:
                            {
                                Node node = new75();
                                push(goTo(66), node, false);
                            }
                            break;
                        case 76:
                            {
                                Node node = new76();
                                push(goTo(32), node, true);
                            }
                            break;
                        case 77:
                            {
                                Node node = new77();
                                push(goTo(33), node, true);
                            }
                            break;
                        case 78:
                            {
                                Node node = new78();
                                push(goTo(34), node, true);
                            }
                            break;
                        case 79:
                            {
                                Node node = new79();
                                push(goTo(34), node, true);
                            }
                            break;
                        case 80:
                            {
                                Node node = new80();
                                push(goTo(35), node, true);
                            }
                            break;
                        case 81:
                            {
                                Node node = new81();
                                push(goTo(35), node, true);
                            }
                            break;
                        case 82:
                            {
                                Node node = new82();
                                push(goTo(36), node, true);
                            }
                            break;
                        case 83:
                            {
                                Node node = new83();
                                push(goTo(36), node, true);
                            }
                            break;
                        case 84:
                            {
                                Node node = new84();
                                push(goTo(37), node, true);
                            }
                            break;
                        case 85:
                            {
                                Node node = new85();
                                push(goTo(38), node, true);
                            }
                            break;
                        case 86:
                            {
                                Node node = new86();
                                push(goTo(39), node, true);
                            }
                            break;
                        case 87:
                            {
                                Node node = new87();
                                push(goTo(39), node, true);
                            }
                            break;
                        case 88:
                            {
                                Node node = new88();
                                push(goTo(40), node, true);
                            }
                            break;
                        case 89:
                            {
                                Node node = new89();
                                push(goTo(41), node, true);
                            }
                            break;
                        case 90:
                            {
                                Node node = new90();
                                push(goTo(42), node, true);
                            }
                            break;
                        case 91:
                            {
                                Node node = new91();
                                push(goTo(42), node, true);
                            }
                            break;
                        case 92:
                            {
                                Node node = new92();
                                push(goTo(43), node, true);
                            }
                            break;
                        case 93:
                            {
                                Node node = new93();
                                push(goTo(43), node, true);
                            }
                            break;
                        case 94:
                            {
                                Node node = new94();
                                push(goTo(44), node, true);
                            }
                            break;
                        case 95:
                            {
                                Node node = new95();
                                push(goTo(44), node, true);
                            }
                            break;
                        case 96:
                            {
                                Node node = new96();
                                push(goTo(45), node, true);
                            }
                            break;
                        case 97:
                            {
                                Node node = new97();
                                push(goTo(46), node, true);
                            }
                            break;
                        case 98:
                            {
                                Node node = new98();
                                push(goTo(46), node, true);
                            }
                            break;
                        case 99:
                            {
                                Node node = new99();
                                push(goTo(46), node, true);
                            }
                            break;
                        case 100:
                            {
                                Node node = new100();
                                push(goTo(46), node, true);
                            }
                            break;
                        case 101:
                            {
                                Node node = new101();
                                push(goTo(47), node, true);
                            }
                            break;
                        case 102:
                            {
                                Node node = new102();
                                push(goTo(47), node, true);
                            }
                            break;
                        case 103:
                            {
                                Node node = new103();
                                push(goTo(48), node, true);
                            }
                            break;
                        case 104:
                            {
                                Node node = new104();
                                push(goTo(48), node, true);
                            }
                            break;
                        case 105:
                            {
                                Node node = new105();
                                push(goTo(48), node, true);
                            }
                            break;
                        case 106:
                            {
                                Node node = new106();
                                push(goTo(48), node, true);
                            }
                            break;
                        case 107:
                            {
                                Node node = new107();
                                push(goTo(49), node, true);
                            }
                            break;
                        case 108:
                            {
                                Node node = new108();
                                push(goTo(50), node, true);
                            }
                            break;
                        case 109:
                            {
                                Node node = new109();
                                push(goTo(51), node, true);
                            }
                            break;
                        case 110:
                            {
                                Node node = new110();
                                push(goTo(51), node, true);
                            }
                            break;
                        case 111:
                            {
                                Node node = new111();
                                push(goTo(52), node, true);
                            }
                            break;
                        case 112:
                            {
                                Node node = new112();
                                push(goTo(52), node, true);
                            }
                            break;
                        case 113:
                            {
                                Node node = new113();
                                push(goTo(52), node, true);
                            }
                            break;
                        case 114:
                            {
                                Node node = new114();
                                push(goTo(52), node, true);
                            }
                            break;
                        case 115:
                            {
                                Node node = new115();
                                push(goTo(53), node, true);
                            }
                            break;
                        case 116:
                            {
                                Node node = new116();
                                push(goTo(53), node, true);
                            }
                            break;
                        case 117:
                            {
                                Node node = new117();
                                push(goTo(53), node, true);
                            }
                            break;
                        case 118:
                            {
                                Node node = new118();
                                push(goTo(53), node, true);
                            }
                            break;
                        case 119:
                            {
                                Node node = new119();
                                push(goTo(54), node, true);
                            }
                            break;
                        case 120:
                            {
                                Node node = new120();
                                push(goTo(54), node, true);
                            }
                            break;
                        case 121:
                            {
                                Node node = new121();
                                push(goTo(54), node, true);
                            }
                            break;
                        case 122:
                            {
                                Node node = new122();
                                push(goTo(54), node, true);
                            }
                            break;
                        case 123:
                            {
                                Node node = new123();
                                push(goTo(55), node, true);
                            }
                            break;
                        case 124:
                            {
                                Node node = new124();
                                push(goTo(56), node, true);
                            }
                            break;
                        case 125:
                            {
                                Node node = new125();
                                push(goTo(57), node, true);
                            }
                            break;
                        case 126:
                            {
                                Node node = new126();
                                push(goTo(57), node, true);
                            }
                            break;
                        case 127:
                            {
                                Node node = new127();
                                push(goTo(58), node, true);
                            }
                            break;
                        case 128:
                            {
                                Node node = new128();
                                push(goTo(58), node, true);
                            }
                            break;
                        case 129:
                            {
                                Node node = new129();
                                push(goTo(58), node, true);
                            }
                            break;
                        case 130:
                            {
                                Node node = new130();
                                push(goTo(59), node, true);
                            }
                            break;
                        case 131:
                            {
                                Node node = new131();
                                push(goTo(59), node, true);
                            }
                            break;
                        case 132:
                            {
                                Node node = new132();
                                push(goTo(60), node, true);
                            }
                            break;
                        case 133:
                            {
                                Node node = new133();
                                push(goTo(60), node, true);
                            }
                            break;
                        case 134:
                            {
                                Node node = new134();
                                push(goTo(60), node, true);
                            }
                            break;
                    }
                    break;
                case ACCEPT:
                    {
                        EOF node2 = (EOF) lexer.next();
                        PScript node1 = (PScript) pop();
                        Start node = new Start(node1, node2);
                        return node;
                    }
                case ERROR:
                    throw new ParserException(last_token, "[" + last_line + "," + last_pos + "] " + errorMessages[errors[action[1]]]);
            }
        }
    }

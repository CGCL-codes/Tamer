    protected Token getToken() throws IOException, LexerException {
        int dfa_state = 0;
        int start_pos = this.pos;
        int start_line = this.line;
        int accept_state = -1;
        int accept_token = -1;
        int accept_length = -1;
        int accept_pos = -1;
        int accept_line = -1;
        @SuppressWarnings("hiding") int[][][] gotoTable = Lexer.gotoTable[this.state.id()];
        @SuppressWarnings("hiding") int[] accept = Lexer.accept[this.state.id()];
        this.text.setLength(0);
        while (true) {
            int c = getChar();
            if (c != -1) {
                switch(c) {
                    case 10:
                        if (this.cr) {
                            this.cr = false;
                        } else {
                            this.line++;
                            this.pos = 0;
                        }
                        break;
                    case 13:
                        this.line++;
                        this.pos = 0;
                        this.cr = true;
                        break;
                    default:
                        this.pos++;
                        this.cr = false;
                        break;
                }
                this.text.append((char) c);
                do {
                    int oldState = (dfa_state < -1) ? (-2 - dfa_state) : dfa_state;
                    dfa_state = -1;
                    int[][] tmp1 = gotoTable[oldState];
                    int low = 0;
                    int high = tmp1.length - 1;
                    while (low <= high) {
                        int middle = (low + high) / 2;
                        int[] tmp2 = tmp1[middle];
                        if (c < tmp2[0]) {
                            high = middle - 1;
                        } else if (c > tmp2[1]) {
                            low = middle + 1;
                        } else {
                            dfa_state = tmp2[2];
                            break;
                        }
                    }
                } while (dfa_state < -1);
            } else {
                dfa_state = -1;
            }
            if (dfa_state >= 0) {
                if (accept[dfa_state] != -1) {
                    accept_state = dfa_state;
                    accept_token = accept[dfa_state];
                    accept_length = this.text.length();
                    accept_pos = this.pos;
                    accept_line = this.line;
                }
            } else {
                if (accept_state != -1) {
                    switch(accept_token) {
                        case 0:
                            {
                                @SuppressWarnings("hiding") Token token = new0(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 14:
                                        state = State.LOAD_VAR;
                                        break;
                                    case 10:
                                        state = State.SET_VAR;
                                        break;
                                    case 7:
                                        state = State.WITH_VAR;
                                        break;
                                }
                                return token;
                            }
                        case 1:
                            {
                                @SuppressWarnings("hiding") Token token = new1(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 17:
                                        state = State.IF_VAR;
                                        break;
                                }
                                return token;
                            }
                        case 2:
                            {
                                @SuppressWarnings("hiding") Token token = new2(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 17:
                                        state = State.IF_VAR;
                                        break;
                                }
                                return token;
                            }
                        case 3:
                            {
                                @SuppressWarnings("hiding") Token token = new3(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 17:
                                        state = State.IF_VAR;
                                        break;
                                }
                                return token;
                            }
                        case 4:
                            {
                                @SuppressWarnings("hiding") Token token = new4(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.DEFAULT;
                                        break;
                                }
                                return token;
                            }
                        case 5:
                            {
                                @SuppressWarnings("hiding") Token token = new5(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.VAR;
                                        break;
                                }
                                return token;
                            }
                        case 6:
                            {
                                @SuppressWarnings("hiding") Token token = new6(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 1:
                                        state = State.DEFAULT;
                                        break;
                                }
                                return token;
                            }
                        case 7:
                            {
                                @SuppressWarnings("hiding") Token token = new7(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 23:
                                        state = State.FOR_VAR;
                                        break;
                                }
                                return token;
                            }
                        case 8:
                            {
                                @SuppressWarnings("hiding") Token token = new8(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.BLOCK_TAG;
                                        break;
                                }
                                return token;
                            }
                        case 9:
                            {
                                @SuppressWarnings("hiding") Token token = new9(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.LOAD_VAR;
                                        break;
                                }
                                return token;
                            }
                        case 10:
                            {
                                @SuppressWarnings("hiding") Token token = new10(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.INCLUDE_TAG;
                                        break;
                                }
                                return token;
                            }
                        case 11:
                            {
                                @SuppressWarnings("hiding") Token token = new11(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.FIRSTOF_VAR;
                                        break;
                                }
                                return token;
                            }
                        case 12:
                            {
                                @SuppressWarnings("hiding") Token token = new12(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.FOR_TAG;
                                        break;
                                }
                                return token;
                            }
                        case 13:
                            {
                                @SuppressWarnings("hiding") Token token = new13(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.IF_VAR;
                                        break;
                                }
                                return token;
                            }
                        case 14:
                            {
                                @SuppressWarnings("hiding") Token token = new14(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.IFEQ_VAR;
                                        break;
                                }
                                return token;
                            }
                        case 15:
                            {
                                @SuppressWarnings("hiding") Token token = new15(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.FILTER_VAR;
                                        break;
                                }
                                return token;
                            }
                        case 16:
                            {
                                @SuppressWarnings("hiding") Token token = new16(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.WITH_VAR;
                                        break;
                                }
                                return token;
                            }
                        case 17:
                            {
                                @SuppressWarnings("hiding") Token token = new17(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.SET_VAR;
                                        break;
                                }
                                return token;
                            }
                        case 18:
                            {
                                @SuppressWarnings("hiding") Token token = new18(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.NOW_TAG;
                                        break;
                                }
                                return token;
                            }
                        case 19:
                            {
                                @SuppressWarnings("hiding") Token token = new19(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.EXTENDS_TAG;
                                        break;
                                }
                                return token;
                            }
                        case 20:
                            {
                                @SuppressWarnings("hiding") Token token = new20(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.TEMPLATE_TAG;
                                        break;
                                }
                                return token;
                            }
                        case 21:
                            {
                                @SuppressWarnings("hiding") Token token = new21(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.DEFAULT;
                                        break;
                                }
                                return token;
                            }
                        case 22:
                            {
                                @SuppressWarnings("hiding") Token token = new22(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.DEFAULT;
                                        break;
                                }
                                return token;
                            }
                        case 23:
                            {
                                @SuppressWarnings("hiding") Token token = new23(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.DEFAULT;
                                        break;
                                }
                                return token;
                            }
                        case 24:
                            {
                                @SuppressWarnings("hiding") Token token = new24(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.COMMENT_BLOCK;
                                        break;
                                }
                                return token;
                            }
                        case 25:
                            {
                                @SuppressWarnings("hiding") Token token = new25(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 27:
                                        state = State.DEFAULT;
                                        break;
                                    case 28:
                                        state = State.DEFAULT;
                                        break;
                                    case 4:
                                        state = State.DEFAULT;
                                        break;
                                    case 14:
                                        state = State.DEFAULT;
                                        break;
                                    case 13:
                                        state = State.DEFAULT;
                                        break;
                                    case 34:
                                        state = State.DEFAULT;
                                        break;
                                    case 33:
                                        state = State.DEFAULT;
                                        break;
                                    case 1:
                                        state = State.DEFAULT;
                                        break;
                                }
                                return token;
                            }
                        case 26:
                            {
                                @SuppressWarnings("hiding") Token token = new26(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 3:
                                        state = State.DEFAULT;
                                        break;
                                    case 31:
                                        state = State.DEFAULT;
                                        break;
                                    case 24:
                                        state = State.DEFAULT;
                                        break;
                                    case 20:
                                        state = State.DEFAULT;
                                        break;
                                    case 17:
                                        state = State.DEFAULT;
                                        break;
                                    case 10:
                                        state = State.DEFAULT;
                                        break;
                                    case 7:
                                        state = State.DEFAULT;
                                        break;
                                }
                                return token;
                            }
                        case 27:
                            {
                                @SuppressWarnings("hiding") Token token = new27(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.DEFAULT;
                                        break;
                                }
                                return token;
                            }
                        case 28:
                            {
                                @SuppressWarnings("hiding") Token token = new28(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.DEFAULT;
                                        break;
                                }
                                return token;
                            }
                        case 29:
                            {
                                @SuppressWarnings("hiding") Token token = new29(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.DEFAULT;
                                        break;
                                }
                                return token;
                            }
                        case 30:
                            {
                                @SuppressWarnings("hiding") Token token = new30(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.DEFAULT;
                                        break;
                                }
                                return token;
                            }
                        case 31:
                            {
                                @SuppressWarnings("hiding") Token token = new31(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 5:
                                        state = State.DEFAULT;
                                        break;
                                }
                                return token;
                            }
                        case 32:
                            {
                                @SuppressWarnings("hiding") Token token = new32(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 5:
                                        state = State.COMMENT_BLOCK;
                                        break;
                                }
                                return token;
                            }
                        case 33:
                            {
                                @SuppressWarnings("hiding") Token token = new33(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 27:
                                        state = State.EXTENDS_TAG;
                                        break;
                                    case 6:
                                        state = State.VAR;
                                        break;
                                    case 32:
                                        state = State.FILTER_VAR;
                                        break;
                                    case 30:
                                        state = State.FIRSTOF_VAR;
                                        break;
                                    case 28:
                                        state = State.FIRSTOF_VAR;
                                        break;
                                    case 20:
                                        state = State.IFEQ_VAR;
                                        break;
                                    case 4:
                                        state = State.INCLUDE_TAG;
                                        break;
                                    case 16:
                                        state = State.LOAD_VAR;
                                        break;
                                    case 13:
                                        state = State.LOAD_AS;
                                        break;
                                    case 34:
                                        state = State.NOW_TAG;
                                        break;
                                    case 12:
                                        state = State.SET_VAR;
                                        break;
                                    case 9:
                                        state = State.WITH_VAR;
                                        break;
                                }
                                return token;
                            }
                        case 34:
                            {
                                @SuppressWarnings("hiding") Token token = new34(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 28:
                                        state = State.FIRSTOF_MEMBER;
                                        break;
                                    case 24:
                                        state = State.FOR_MEMBER;
                                        break;
                                    case 20:
                                        state = State.IFEQ_MEMBER;
                                        break;
                                    case 17:
                                        state = State.IF_MEMBER;
                                        break;
                                    case 13:
                                        state = State.LOAD_MEMBER;
                                        break;
                                    case 10:
                                        state = State.SET_MEMBER;
                                        break;
                                    case 1:
                                        state = State.VAR_MEMBER;
                                        break;
                                    case 7:
                                        state = State.WITH_MEMBER;
                                        break;
                                }
                                return token;
                            }
                        case 35:
                            {
                                @SuppressWarnings("hiding") Token token = new35(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 3:
                                        state = State.BLOCK_TAG;
                                        break;
                                    case 31:
                                        state = State.FILTER_VAR;
                                        break;
                                    case 28:
                                        state = State.FIRSTOF_VAR;
                                        break;
                                    case 23:
                                        state = State.FOR_TAG;
                                        break;
                                    case 24:
                                        state = State.FOR_VAR;
                                        break;
                                    case 20:
                                        state = State.IFEQ_VAR;
                                        break;
                                    case 17:
                                        state = State.IF_VAR;
                                        break;
                                    case 4:
                                        state = State.INCLUDE_TAG;
                                        break;
                                    case 13:
                                        state = State.LOAD_VAR;
                                        break;
                                    case 10:
                                        state = State.SET_VAR;
                                        break;
                                    case 33:
                                        state = State.TEMPLATE_TAG;
                                        break;
                                    case 1:
                                        state = State.VAR;
                                        break;
                                    case 7:
                                        state = State.WITH_VAR;
                                        break;
                                }
                                return token;
                            }
                        case 36:
                            {
                                @SuppressWarnings("hiding") Token token = new36(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 29:
                                        state = State.FIRSTOF_VAR;
                                        break;
                                    case 25:
                                        state = State.FOR_VAR;
                                        break;
                                    case 21:
                                        state = State.IFEQ_VAR;
                                        break;
                                    case 18:
                                        state = State.IF_VAR;
                                        break;
                                    case 4:
                                        state = State.INCLUDE_TAG;
                                        break;
                                    case 15:
                                        state = State.LOAD_VAR;
                                        break;
                                    case 11:
                                        state = State.SET_VAR;
                                        break;
                                    case 2:
                                        state = State.VAR;
                                        break;
                                    case 8:
                                        state = State.WITH_VAR;
                                        break;
                                }
                                return token;
                            }
                        case 37:
                            {
                                @SuppressWarnings("hiding") Token token = new37(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 3:
                                        state = State.BLOCK_TAG;
                                        break;
                                    case 27:
                                        state = State.EXTENDS_TAG;
                                        break;
                                    case 6:
                                        state = State.FILTER;
                                        break;
                                    case 31:
                                        state = State.FILTER_VAR;
                                        break;
                                    case 28:
                                        state = State.FIRSTOF_VAR;
                                        break;
                                    case 23:
                                        state = State.FOR_TAG;
                                        break;
                                    case 24:
                                        state = State.FOR_VAR;
                                        break;
                                    case 20:
                                        state = State.IFEQ_VAR;
                                        break;
                                    case 19:
                                        state = State.IF_FILTER;
                                        break;
                                    case 17:
                                        state = State.IF_VAR;
                                        break;
                                    case 4:
                                        state = State.INCLUDE_TAG;
                                        break;
                                    case 14:
                                        state = State.LOAD_AS;
                                        break;
                                    case 16:
                                        state = State.LOAD_FILTER;
                                        break;
                                    case 13:
                                        state = State.LOAD_VAR;
                                        break;
                                    case 34:
                                        state = State.NOW_TAG;
                                        break;
                                    case 12:
                                        state = State.SET_FILTER;
                                        break;
                                    case 10:
                                        state = State.SET_VAR;
                                        break;
                                    case 33:
                                        state = State.TEMPLATE_TAG;
                                        break;
                                    case 1:
                                        state = State.VAR;
                                        break;
                                    case 9:
                                        state = State.WITH_FILTER;
                                        break;
                                    case 7:
                                        state = State.WITH_VAR;
                                        break;
                                }
                                return token;
                            }
                        case 38:
                            {
                                @SuppressWarnings("hiding") Token token = new38(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.DEFAULT;
                                        break;
                                }
                                return token;
                            }
                        case 39:
                            {
                                @SuppressWarnings("hiding") Token token = new39(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 31:
                                        state = State.FILTER_VAR;
                                        break;
                                    case 28:
                                        state = State.FIRSTOF_VAR;
                                        break;
                                    case 24:
                                        state = State.FOR_VAR;
                                        break;
                                    case 20:
                                        state = State.IFEQ_VAR;
                                        break;
                                    case 17:
                                        state = State.IF_VAR;
                                        break;
                                    case 13:
                                        state = State.LOAD_VAR;
                                        break;
                                    case 10:
                                        state = State.SET_VAR;
                                        break;
                                    case 1:
                                        state = State.VAR;
                                        break;
                                    case 7:
                                        state = State.WITH_VAR;
                                        break;
                                }
                                return token;
                            }
                        case 40:
                            {
                                @SuppressWarnings("hiding") Token token = new40(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 28:
                                        state = State.FIRSTOF_VAR;
                                        break;
                                    case 24:
                                        state = State.FOR_VAR;
                                        break;
                                    case 20:
                                        state = State.IFEQ_VAR;
                                        break;
                                    case 17:
                                        state = State.IF_VAR;
                                        break;
                                    case 13:
                                        state = State.LOAD_VAR;
                                        break;
                                    case 1:
                                        state = State.VAR;
                                        break;
                                    case 7:
                                        state = State.WITH_VAR;
                                        break;
                                }
                                return token;
                            }
                        case 41:
                            {
                                @SuppressWarnings("hiding") Token token = new41(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 31:
                                        state = State.FILTER_FILTER;
                                        break;
                                    case 28:
                                        state = State.FIRSTOF_FILTER;
                                        break;
                                    case 24:
                                        state = State.FOR_FILTER;
                                        break;
                                    case 20:
                                        state = State.IFEQ_FILTER;
                                        break;
                                    case 17:
                                        state = State.IF_FILTER;
                                        break;
                                    case 13:
                                        state = State.LOAD_FILTER;
                                        break;
                                    case 10:
                                        state = State.SET_FILTER;
                                        break;
                                    case 1:
                                        state = State.FILTER;
                                        break;
                                    case 7:
                                        state = State.WITH_FILTER;
                                        break;
                                }
                                return token;
                            }
                        case 42:
                            {
                                @SuppressWarnings("hiding") Token token = new42(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                this.pos = accept_pos;
                                this.line = accept_line;
                                switch(state.id()) {
                                    case 6:
                                        state = State.VAR;
                                        break;
                                    case 32:
                                        state = State.FILTER_VAR;
                                        break;
                                    case 30:
                                        state = State.FIRSTOF_VAR;
                                        break;
                                    case 26:
                                        state = State.FOR_VAR;
                                        break;
                                    case 22:
                                        state = State.IFEQ_VAR;
                                        break;
                                    case 19:
                                        state = State.IF_VAR;
                                        break;
                                    case 16:
                                        state = State.LOAD_VAR;
                                        break;
                                    case 12:
                                        state = State.SET_VAR;
                                        break;
                                    case 9:
                                        state = State.WITH_VAR;
                                        break;
                                }
                                return token;
                            }
                    }
                } else {
                    if (this.text.length() > 0) {
                        throw new LexerException("[" + (start_line + 1) + "," + (start_pos + 1) + "]" + " Unknown token: " + this.text);
                    }
                    @SuppressWarnings("hiding") EOF token = new EOF(start_line + 1, start_pos + 1);
                    return token;
                }
            }
        }
    }

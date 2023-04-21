package org.seqtagutils.search;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class SearchLexer extends Lexer {

    public static final int TERM = 4;

    public static final int LT = 16;

    public static final int LETTER = 19;

    public static final int GTE = 13;

    public static final int WHITESPACE = 23;

    public static final int EQUALS = 11;

    public static final int NOT = 7;

    public static final int AND = 6;

    public static final int EOF = -1;

    public static final int NOT_EQUALS = 12;

    public static final int LTE = 15;

    public static final int LPAREN = 8;

    public static final int QUOTE = 22;

    public static final int RPAREN = 9;

    public static final int IDENTIFIER = 10;

    public static final int OR = 5;

    public static final int GT = 14;

    public static final int QUOTED_STRING = 17;

    public static final int DIGIT = 20;

    public static final int DOT = 21;

    public static final int STRING = 18;

    public SearchLexer() {
        ;
    }

    public SearchLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }

    public SearchLexer(CharStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public String getGrammarFileName() {
        return "src/services/org/seqtagutils/search/Search.g";
    }

    public final void mEQUALS() throws RecognitionException {
        try {
            int _type = EQUALS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match('=');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mNOT_EQUALS() throws RecognitionException {
        try {
            int _type = NOT_EQUALS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match("!=");
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mGT() throws RecognitionException {
        try {
            int _type = GT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match('>');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mLT() throws RecognitionException {
        try {
            int _type = LT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match('<');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mLTE() throws RecognitionException {
        try {
            int _type = LTE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match("<=");
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mGTE() throws RecognitionException {
        try {
            int _type = GTE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match(">=");
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mLPAREN() throws RecognitionException {
        try {
            int _type = LPAREN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match('(');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mRPAREN() throws RecognitionException {
        try {
            int _type = RPAREN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match(')');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mAND() throws RecognitionException {
        try {
            int _type = AND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            int alt1 = 2;
            int LA1_0 = input.LA(1);
            if ((LA1_0 == 'A')) {
                alt1 = 1;
            } else if ((LA1_0 == 'a')) {
                alt1 = 2;
            } else {
                NoViableAltException nvae = new NoViableAltException("", 1, 0, input);
                throw nvae;
            }
            switch(alt1) {
                case 1:
                    {
                        match("AND");
                    }
                    break;
                case 2:
                    {
                        match("and");
                    }
                    break;
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mOR() throws RecognitionException {
        try {
            int _type = OR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            int alt2 = 2;
            int LA2_0 = input.LA(1);
            if ((LA2_0 == 'O')) {
                alt2 = 1;
            } else if ((LA2_0 == 'o')) {
                alt2 = 2;
            } else {
                NoViableAltException nvae = new NoViableAltException("", 2, 0, input);
                throw nvae;
            }
            switch(alt2) {
                case 1:
                    {
                        match("OR");
                    }
                    break;
                case 2:
                    {
                        match("or");
                    }
                    break;
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mNOT() throws RecognitionException {
        try {
            int _type = NOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match("NOT");
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mLETTER() throws RecognitionException {
        try {
            int _type = LETTER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                if ((input.LA(1) >= 'A' && input.LA(1) <= 'Z') || (input.LA(1) >= 'a' && input.LA(1) <= 'z') || (input.LA(1) >= '' && input.LA(1) <= '￾')) {
                    input.consume();
                } else {
                    MismatchedSetException mse = new MismatchedSetException(null, input);
                    recover(mse);
                    throw mse;
                }
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mDIGIT() throws RecognitionException {
        try {
            int _type = DIGIT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                matchRange('0', '9');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mDOT() throws RecognitionException {
        try {
            int _type = DOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match('.');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mQUOTE() throws RecognitionException {
        try {
            int _type = QUOTE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match('\"');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mIDENTIFIER() throws RecognitionException {
        try {
            int _type = IDENTIFIER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                mLETTER();
                loop3: do {
                    int alt3 = 2;
                    int LA3_0 = input.LA(1);
                    if ((LA3_0 == '.' || (LA3_0 >= '0' && LA3_0 <= '9') || (LA3_0 >= 'A' && LA3_0 <= 'Z') || (LA3_0 >= 'a' && LA3_0 <= 'z') || (LA3_0 >= '' && LA3_0 <= '￾'))) {
                        alt3 = 1;
                    }
                    switch(alt3) {
                        case 1:
                            {
                                if (input.LA(1) == '.' || (input.LA(1) >= '0' && input.LA(1) <= '9') || (input.LA(1) >= 'A' && input.LA(1) <= 'Z') || (input.LA(1) >= 'a' && input.LA(1) <= 'z') || (input.LA(1) >= '' && input.LA(1) <= '￾')) {
                                    input.consume();
                                } else {
                                    MismatchedSetException mse = new MismatchedSetException(null, input);
                                    recover(mse);
                                    throw mse;
                                }
                            }
                            break;
                        default:
                            break loop3;
                    }
                } while (true);
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                int cnt4 = 0;
                loop4: do {
                    int alt4 = 2;
                    int LA4_0 = input.LA(1);
                    if ((LA4_0 == '.' || (LA4_0 >= '0' && LA4_0 <= '9') || (LA4_0 >= 'A' && LA4_0 <= 'Z') || (LA4_0 >= 'a' && LA4_0 <= 'z') || (LA4_0 >= '' && LA4_0 <= '￾'))) {
                        alt4 = 1;
                    }
                    switch(alt4) {
                        case 1:
                            {
                                if (input.LA(1) == '.' || (input.LA(1) >= '0' && input.LA(1) <= '9') || (input.LA(1) >= 'A' && input.LA(1) <= 'Z') || (input.LA(1) >= 'a' && input.LA(1) <= 'z') || (input.LA(1) >= '' && input.LA(1) <= '￾')) {
                                    input.consume();
                                } else {
                                    MismatchedSetException mse = new MismatchedSetException(null, input);
                                    recover(mse);
                                    throw mse;
                                }
                            }
                            break;
                        default:
                            if (cnt4 >= 1) break loop4;
                            EarlyExitException eee = new EarlyExitException(4, input);
                            throw eee;
                    }
                    cnt4++;
                } while (true);
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mQUOTED_STRING() throws RecognitionException {
        try {
            int _type = QUOTED_STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                mQUOTE();
                loop5: do {
                    int alt5 = 2;
                    int LA5_0 = input.LA(1);
                    if (((LA5_0 >= ' ' && LA5_0 <= '\b') || (LA5_0 >= '' && LA5_0 <= '\f') || (LA5_0 >= '' && LA5_0 <= '!') || (LA5_0 >= '#' && LA5_0 <= '\'') || (LA5_0 >= '*' && LA5_0 <= '￿'))) {
                        alt5 = 1;
                    }
                    switch(alt5) {
                        case 1:
                            {
                                if ((input.LA(1) >= ' ' && input.LA(1) <= '\b') || (input.LA(1) >= '' && input.LA(1) <= '\f') || (input.LA(1) >= '' && input.LA(1) <= '!') || (input.LA(1) >= '#' && input.LA(1) <= '\'') || (input.LA(1) >= '*' && input.LA(1) <= '￿')) {
                                    input.consume();
                                } else {
                                    MismatchedSetException mse = new MismatchedSetException(null, input);
                                    recover(mse);
                                    throw mse;
                                }
                            }
                            break;
                        default:
                            break loop5;
                    }
                } while (true);
                mQUOTE();
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mWHITESPACE() throws RecognitionException {
        try {
            int _type = WHITESPACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                int cnt6 = 0;
                loop6: do {
                    int alt6 = 2;
                    int LA6_0 = input.LA(1);
                    if (((LA6_0 >= '\t' && LA6_0 <= '\n') || LA6_0 == '\r' || LA6_0 == ' ')) {
                        alt6 = 1;
                    }
                    switch(alt6) {
                        case 1:
                            {
                                if ((input.LA(1) >= '\t' && input.LA(1) <= '\n') || input.LA(1) == '\r' || input.LA(1) == ' ') {
                                    input.consume();
                                } else {
                                    MismatchedSetException mse = new MismatchedSetException(null, input);
                                    recover(mse);
                                    throw mse;
                                }
                            }
                            break;
                        default:
                            if (cnt6 >= 1) break loop6;
                            EarlyExitException eee = new EarlyExitException(6, input);
                            throw eee;
                    }
                    cnt6++;
                } while (true);
                _channel = HIDDEN;
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public void mTokens() throws RecognitionException {
        int alt7 = 19;
        alt7 = dfa7.predict(input);
        switch(alt7) {
            case 1:
                {
                    mEQUALS();
                }
                break;
            case 2:
                {
                    mNOT_EQUALS();
                }
                break;
            case 3:
                {
                    mGT();
                }
                break;
            case 4:
                {
                    mLT();
                }
                break;
            case 5:
                {
                    mLTE();
                }
                break;
            case 6:
                {
                    mGTE();
                }
                break;
            case 7:
                {
                    mLPAREN();
                }
                break;
            case 8:
                {
                    mRPAREN();
                }
                break;
            case 9:
                {
                    mAND();
                }
                break;
            case 10:
                {
                    mOR();
                }
                break;
            case 11:
                {
                    mNOT();
                }
                break;
            case 12:
                {
                    mLETTER();
                }
                break;
            case 13:
                {
                    mDIGIT();
                }
                break;
            case 14:
                {
                    mDOT();
                }
                break;
            case 15:
                {
                    mQUOTE();
                }
                break;
            case 16:
                {
                    mIDENTIFIER();
                }
                break;
            case 17:
                {
                    mSTRING();
                }
                break;
            case 18:
                {
                    mQUOTED_STRING();
                }
                break;
            case 19:
                {
                    mWHITESPACE();
                }
                break;
        }
    }

    protected DFA7 dfa7 = new DFA7(this);

    static final String DFA7_eotS = "\3￿\1\22\1\24\2￿\6\26\1\34\1\36\1\37\5￿\1\42\1￿" + "\2\42\2\44\1\42\5￿\1\46\1￿\1\46\1￿\1\47\2￿";

    static final String DFA7_eofS = "\50￿";

    static final String DFA7_minS = "\1\11\2￿\2\75\2￿\10\56\1\0\5￿\1\56\1￿\5\56" + "\5￿\1\56\1￿\1\56\1￿\1\56\2￿";

    static final String DFA7_maxS = "\1￾\2￿\2\75\2￿\10￾\1￿\5￿\1￾" + "\1￿\5￾\5￿\1￾\1￿\1￾\1￿\1￾" + "\2￿";

    static final String DFA7_acceptS = "\1￿\1\1\1\2\2￿\1\7\1\10\11￿\1\23\1\6\1\3\1\5\1\4" + "\1￿\1\14\5￿\1\15\1\21\1\16\1\17\1\22\1￿\1\20\1￿" + "\1\12\1￿\1\11\1\13";

    static final String DFA7_specialS = "\17￿\1\0\30￿}>";

    static final String[] DFA7_transitionS = { "\2\20\2￿\1\20\22￿\1\20\1\2\1\17\5￿\1\5\1\6\4" + "￿\1\16\1￿\12\15\2￿\1\4\1\1\1\3\2￿\1\7\14" + "\14\1\13\1\11\13\14\6￿\1\10\15\14\1\12\13\14\5￿ｿ" + "\14", "", "", "\1\21", "\1\23", "", "", "\1\27\1￿\12\27\7￿\15\27\1\25\14\27\6￿\32\27" + "\5￿ｿ\27", "\1\27\1￿\12\27\7￿\32\27\6￿\15\27\1\30\14\27" + "\5￿ｿ\27", "\1\27\1￿\12\27\7￿\21\27\1\31\10\27\6￿\32\27" + "\5￿ｿ\27", "\1\27\1￿\12\27\7￿\32\27\6￿\21\27\1\32\10\27" + "\5￿ｿ\27", "\1\27\1￿\12\27\7￿\16\27\1\33\13\27\6￿\32\27" + "\5￿ｿ\27", "\1\27\1￿\12\27\7￿\32\27\6￿\32\27\5￿ｿ" + "\27", "\1\35\1￿\12\35\7￿\32\35\6￿\32\35\5￿ｿ" + "\35", "\1\35\1￿\12\35\7￿\32\35\6￿\32\35\5￿ｿ" + "\35", "\11\40\2￿\2\40\1￿\32\40\2￿ￖ\40", "", "", "", "", "", "\1\27\1￿\12\27\7￿\3\27\1\41\26\27\6￿\32\27" + "\5￿ｿ\27", "", "\1\27\1￿\12\27\7￿\32\27\6￿\32\27\5￿ｿ" + "\27", "\1\27\1￿\12\27\7￿\32\27\6￿\3\27\1\43\26\27" + "\5￿ｿ\27", "\1\27\1￿\12\27\7￿\32\27\6￿\32\27\5￿ｿ" + "\27", "\1\27\1￿\12\27\7￿\32\27\6￿\32\27\5￿ｿ" + "\27", "\1\27\1￿\12\27\7￿\23\27\1\45\6\27\6￿\32\27" + "\5￿ｿ\27", "", "", "", "", "", "\1\27\1￿\12\27\7￿\32\27\6￿\32\27\5￿ｿ" + "\27", "", "\1\27\1￿\12\27\7￿\32\27\6￿\32\27\5￿ｿ" + "\27", "", "\1\27\1￿\12\27\7￿\32\27\6￿\32\27\5￿ｿ" + "\27", "", "" };

    static final short[] DFA7_eot = DFA.unpackEncodedString(DFA7_eotS);

    static final short[] DFA7_eof = DFA.unpackEncodedString(DFA7_eofS);

    static final char[] DFA7_min = DFA.unpackEncodedStringToUnsignedChars(DFA7_minS);

    static final char[] DFA7_max = DFA.unpackEncodedStringToUnsignedChars(DFA7_maxS);

    static final short[] DFA7_accept = DFA.unpackEncodedString(DFA7_acceptS);

    static final short[] DFA7_special = DFA.unpackEncodedString(DFA7_specialS);

    static final short[][] DFA7_transition;

    static {
        int numStates = DFA7_transitionS.length;
        DFA7_transition = new short[numStates][];
        for (int i = 0; i < numStates; i++) {
            DFA7_transition[i] = DFA.unpackEncodedString(DFA7_transitionS[i]);
        }
    }

    class DFA7 extends DFA {

        public DFA7(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 7;
            this.eot = DFA7_eot;
            this.eof = DFA7_eof;
            this.min = DFA7_min;
            this.max = DFA7_max;
            this.accept = DFA7_accept;
            this.special = DFA7_special;
            this.transition = DFA7_transition;
        }

        public String getDescription() {
            return "1:1: Tokens : ( EQUALS | NOT_EQUALS | GT | LT | LTE | GTE | LPAREN | RPAREN | AND | OR | NOT | LETTER | DIGIT | DOT | QUOTE | IDENTIFIER | STRING | QUOTED_STRING | WHITESPACE );";
        }

        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
            int _s = s;
            switch(s) {
                case 0:
                    int LA7_15 = input.LA(1);
                    s = -1;
                    if (((LA7_15 >= ' ' && LA7_15 <= '\b') || (LA7_15 >= '' && LA7_15 <= '\f') || (LA7_15 >= '' && LA7_15 <= '\'') || (LA7_15 >= '*' && LA7_15 <= '￿'))) {
                        s = 32;
                    } else s = 31;
                    if (s >= 0) return s;
                    break;
            }
            NoViableAltException nvae = new NoViableAltException(getDescription(), 7, _s, input);
            error(nvae);
            throw nvae;
        }
    }
}

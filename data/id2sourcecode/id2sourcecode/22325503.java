    private String buildScannerCpp(FiniteAutomata fa, Options options) {
        StringBuffer result = new StringBuffer();
        String classname = options.scannerName;
        result.append("#include \"" + classname + ".h\"\n\n");
        if (!sensitive) result.append("#include <cctype>\n\n");
        result.append(openNamespace(options));
        String inType;
        String inInit;
        if (options.input == STREAM) {
            inType = "std::istream &";
            inInit = "    std::istreambuf_iterator<char> in(input);\n" + "    std::istreambuf_iterator<char> eof;\n" + "\n" + "    this->input.assign(in, eof);\n" + "\n" + "";
        } else if (options.input == STRING) {
            inType = "const char *";
            inInit = "    this->input = input;\n";
        } else {
            inType = null;
            inInit = null;
        }
        String funcs = "void " + classname + "::setInput(" + inType + "input)\n" + "{\n" + inInit + "    setPosition(0);\n" + "}\n" + "\n" + "Token *" + classname + "::nextToken() throw (LexicalError)\n" + "{\n" + "    if ( ! hasInput() )\n" + "        return 0;\n" + "\n" + "    unsigned start = position;\n" + "\n" + "    int state = 0;\n" + "    int oldState = 0;\n" + "    int endState = -1;\n" + "    int end = -1;\n" + (fa.hasContext() ? "    int ctxtState = -1;\n" + "    int ctxtEnd = -1;\n" : "") + "\n" + "    while (hasInput())\n" + "    {\n" + "        oldState = state;\n" + "        state = nextState(nextChar(), state);\n" + "\n" + "        if (state < 0)\n" + "            break;\n" + "\n" + "        else\n" + "        {\n" + "            if (tokenForState(state) >= 0)\n" + "            {\n" + "                endState = state;\n" + "                end = position;\n" + "            }\n" + (fa.hasContext() ? "            if (SCANNER_CONTEXT[state][0] == 1)\n" + "            {\n" + "                ctxtState = state;\n" + "                ctxtEnd = position;\n" + "            }\n" : "") + "        }\n" + "    }\n" + "    if (endState < 0 || (endState != state && tokenForState(oldState) == -2))\n" + "        throw LexicalError(SCANNER_ERROR[oldState], start);\n" + "\n" + (fa.hasContext() ? "    if (ctxtState != -1 && SCANNER_CONTEXT[endState][1] == ctxtState)\n" + "        end = ctxtEnd;\n" + "\n" : "") + "    position = end;\n" + "\n" + "    TokenId token = tokenForState(endState);\n" + "\n" + "    if (token == 0)\n" + "        return nextToken();\n" + "    else\n" + "    {\n" + "            std::string lexeme = input.substr(start, end-start);\n" + (lookup ? "            token = lookupToken(token, lexeme);\n" : "") + "            return new Token(token, lexeme, start);\n" + "    }\n" + "}\n" + "\n" + "int " + classname + "::nextState(unsigned char c, int state) const\n" + "{\n" + nextStateImpl(fa, options) + "}\n" + "\n" + "TokenId " + classname + "::tokenForState(int state) const\n" + "{\n" + "    int token = -1;\n" + "\n" + "    if (state >= 0 && state < STATES_COUNT)\n" + "        token = TOKEN_STATE[state];\n" + "\n" + "    return static_cast<TokenId>(token);\n" + "}\n" + "\n" + (lookup ? "TokenId " + classname + "::lookupToken(TokenId base, const std::string &key)\n" + "{\n" + "    int start = SPECIAL_CASES_INDEXES[base];\n" + "    int end   = SPECIAL_CASES_INDEXES[base+1]-1;\n" + "\n" + (sensitive ? "" : "    std::string key_u = key;\n" + "    for (int i=0; i<key.size(); i++)\n" + "        key_u[i] = std::toupper(key_u[i]);\n" + "\n") + "    while (start <= end)\n" + "    {\n" + "        int half = (start+end)/2;\n" + "        const std::string current = SPECIAL_CASES_KEYS[half];\n" + "\n" + (sensitive ? "        if (current == key)\n" : "        if (current == key_u)\n") + "            return static_cast<TokenId>(SPECIAL_CASES_VALUES[half]);\n" + (sensitive ? "        else if (current < key)\n" : "        else if (current < key_u)\n") + "            start = half+1;\n" + "        else  //(current > key)\n" + "            end = half-1;\n" + "    }\n" + "\n" + "    return base;\n" + "}\n" + "\n" : "");
        result.append(funcs);
        result.append(closeNamespace(options));
        return result.toString();
    }

        public boolean scanExpr(SymbolTable symbolTable, XPath.Tokens tokens, String data, int currentOffset, int endOffset) throws XPathException {
            int nameOffset;
            String nameHandle, prefixHandle;
            boolean starIsMultiplyOperator = false;
            int ch;
            while (true) {
                if (currentOffset == endOffset) {
                    break;
                }
                ch = data.charAt(currentOffset);
                while (ch == ' ' || ch == 0x0A || ch == 0x09 || ch == 0x0D) {
                    if (++currentOffset == endOffset) {
                        break;
                    }
                    ch = data.charAt(currentOffset);
                }
                if (currentOffset == endOffset) {
                    break;
                }
                byte chartype = (ch >= 0x80) ? CHARTYPE_NONASCII : fASCIICharMap[ch];
                switch(chartype) {
                    case CHARTYPE_OPEN_PAREN:
                        addToken(tokens, XPath.Tokens.EXPRTOKEN_OPEN_PAREN);
                        starIsMultiplyOperator = false;
                        if (++currentOffset == endOffset) {
                            break;
                        }
                        break;
                    case CHARTYPE_CLOSE_PAREN:
                        addToken(tokens, XPath.Tokens.EXPRTOKEN_CLOSE_PAREN);
                        starIsMultiplyOperator = true;
                        if (++currentOffset == endOffset) {
                            break;
                        }
                        break;
                    case CHARTYPE_OPEN_BRACKET:
                        addToken(tokens, XPath.Tokens.EXPRTOKEN_OPEN_BRACKET);
                        starIsMultiplyOperator = false;
                        if (++currentOffset == endOffset) {
                            break;
                        }
                        break;
                    case CHARTYPE_CLOSE_BRACKET:
                        addToken(tokens, XPath.Tokens.EXPRTOKEN_CLOSE_BRACKET);
                        starIsMultiplyOperator = true;
                        if (++currentOffset == endOffset) {
                            break;
                        }
                        break;
                    case CHARTYPE_PERIOD:
                        if (currentOffset + 1 == endOffset) {
                            addToken(tokens, XPath.Tokens.EXPRTOKEN_PERIOD);
                            starIsMultiplyOperator = true;
                            currentOffset++;
                            break;
                        }
                        ch = data.charAt(currentOffset + 1);
                        if (ch == '.') {
                            addToken(tokens, XPath.Tokens.EXPRTOKEN_DOUBLE_PERIOD);
                            starIsMultiplyOperator = true;
                            currentOffset += 2;
                        } else if (ch >= '0' && ch <= '9') {
                            addToken(tokens, XPath.Tokens.EXPRTOKEN_NUMBER);
                            starIsMultiplyOperator = true;
                            currentOffset = scanNumber(tokens, data, endOffset, currentOffset);
                        } else if (ch == '/') {
                            addToken(tokens, XPath.Tokens.EXPRTOKEN_PERIOD);
                            starIsMultiplyOperator = true;
                            currentOffset++;
                        } else if (ch == '|') {
                            addToken(tokens, XPath.Tokens.EXPRTOKEN_PERIOD);
                            starIsMultiplyOperator = true;
                            currentOffset++;
                            break;
                        } else if (ch == ' ' || ch == 0x0A || ch == 0x09 || ch == 0x0D) {
                            do {
                                if (++currentOffset == endOffset) {
                                    break;
                                }
                                ch = data.charAt(currentOffset);
                            } while (ch == ' ' || ch == 0x0A || ch == 0x09 || ch == 0x0D);
                            if (currentOffset == endOffset || ch == '|') {
                                addToken(tokens, XPath.Tokens.EXPRTOKEN_PERIOD);
                                starIsMultiplyOperator = true;
                                break;
                            }
                            throw new XPathException("c-general-xpath");
                        } else {
                            throw new XPathException("c-general-xpath");
                        }
                        if (currentOffset == endOffset) {
                            break;
                        }
                        break;
                    case CHARTYPE_ATSIGN:
                        addToken(tokens, XPath.Tokens.EXPRTOKEN_ATSIGN);
                        starIsMultiplyOperator = false;
                        if (++currentOffset == endOffset) {
                            break;
                        }
                        break;
                    case CHARTYPE_COMMA:
                        addToken(tokens, XPath.Tokens.EXPRTOKEN_COMMA);
                        starIsMultiplyOperator = false;
                        if (++currentOffset == endOffset) {
                            break;
                        }
                        break;
                    case CHARTYPE_COLON:
                        if (++currentOffset == endOffset) {
                            return false;
                        }
                        ch = data.charAt(currentOffset);
                        if (ch != ':') {
                            return false;
                        }
                        addToken(tokens, XPath.Tokens.EXPRTOKEN_DOUBLE_COLON);
                        starIsMultiplyOperator = false;
                        if (++currentOffset == endOffset) {
                            break;
                        }
                        break;
                    case CHARTYPE_SLASH:
                        if (++currentOffset == endOffset) {
                            addToken(tokens, XPath.Tokens.EXPRTOKEN_OPERATOR_SLASH);
                            starIsMultiplyOperator = false;
                            break;
                        }
                        ch = data.charAt(currentOffset);
                        if (ch == '/') {
                            addToken(tokens, XPath.Tokens.EXPRTOKEN_OPERATOR_DOUBLE_SLASH);
                            starIsMultiplyOperator = false;
                            if (++currentOffset == endOffset) {
                                break;
                            }
                        } else {
                            addToken(tokens, XPath.Tokens.EXPRTOKEN_OPERATOR_SLASH);
                            starIsMultiplyOperator = false;
                        }
                        break;
                    case CHARTYPE_UNION:
                        addToken(tokens, XPath.Tokens.EXPRTOKEN_OPERATOR_UNION);
                        starIsMultiplyOperator = false;
                        if (++currentOffset == endOffset) {
                            break;
                        }
                        break;
                    case CHARTYPE_PLUS:
                        addToken(tokens, XPath.Tokens.EXPRTOKEN_OPERATOR_PLUS);
                        starIsMultiplyOperator = false;
                        if (++currentOffset == endOffset) {
                            break;
                        }
                        break;
                    case CHARTYPE_MINUS:
                        addToken(tokens, XPath.Tokens.EXPRTOKEN_OPERATOR_MINUS);
                        starIsMultiplyOperator = false;
                        if (++currentOffset == endOffset) {
                            break;
                        }
                        break;
                    case CHARTYPE_EQUAL:
                        addToken(tokens, XPath.Tokens.EXPRTOKEN_OPERATOR_EQUAL);
                        starIsMultiplyOperator = false;
                        if (++currentOffset == endOffset) {
                            break;
                        }
                        break;
                    case CHARTYPE_EXCLAMATION:
                        if (++currentOffset == endOffset) {
                            return false;
                        }
                        ch = data.charAt(currentOffset);
                        if (ch != '=') {
                            return false;
                        }
                        addToken(tokens, XPath.Tokens.EXPRTOKEN_OPERATOR_NOT_EQUAL);
                        starIsMultiplyOperator = false;
                        if (++currentOffset == endOffset) {
                            break;
                        }
                        break;
                    case CHARTYPE_LESS:
                        if (++currentOffset == endOffset) {
                            addToken(tokens, XPath.Tokens.EXPRTOKEN_OPERATOR_LESS);
                            starIsMultiplyOperator = false;
                            break;
                        }
                        ch = data.charAt(currentOffset);
                        if (ch == '=') {
                            addToken(tokens, XPath.Tokens.EXPRTOKEN_OPERATOR_LESS_EQUAL);
                            starIsMultiplyOperator = false;
                            if (++currentOffset == endOffset) {
                                break;
                            }
                        } else {
                            addToken(tokens, XPath.Tokens.EXPRTOKEN_OPERATOR_LESS);
                            starIsMultiplyOperator = false;
                        }
                        break;
                    case CHARTYPE_GREATER:
                        if (++currentOffset == endOffset) {
                            addToken(tokens, XPath.Tokens.EXPRTOKEN_OPERATOR_GREATER);
                            starIsMultiplyOperator = false;
                            break;
                        }
                        ch = data.charAt(currentOffset);
                        if (ch == '=') {
                            addToken(tokens, XPath.Tokens.EXPRTOKEN_OPERATOR_GREATER_EQUAL);
                            starIsMultiplyOperator = false;
                            if (++currentOffset == endOffset) {
                                break;
                            }
                        } else {
                            addToken(tokens, XPath.Tokens.EXPRTOKEN_OPERATOR_GREATER);
                            starIsMultiplyOperator = false;
                        }
                        break;
                    case CHARTYPE_QUOTE:
                        int qchar = ch;
                        if (++currentOffset == endOffset) {
                            return false;
                        }
                        ch = data.charAt(currentOffset);
                        int litOffset = currentOffset;
                        while (ch != qchar) {
                            if (++currentOffset == endOffset) {
                                return false;
                            }
                            ch = data.charAt(currentOffset);
                        }
                        int litLength = currentOffset - litOffset;
                        addToken(tokens, XPath.Tokens.EXPRTOKEN_LITERAL);
                        starIsMultiplyOperator = true;
                        tokens.addToken(symbolTable.addSymbol(data.substring(litOffset, litOffset + litLength)));
                        if (++currentOffset == endOffset) {
                            break;
                        }
                        break;
                    case CHARTYPE_DIGIT:
                        addToken(tokens, XPath.Tokens.EXPRTOKEN_NUMBER);
                        starIsMultiplyOperator = true;
                        currentOffset = scanNumber(tokens, data, endOffset, currentOffset);
                        break;
                    case CHARTYPE_DOLLAR:
                        if (++currentOffset == endOffset) {
                            return false;
                        }
                        nameOffset = currentOffset;
                        currentOffset = scanNCName(data, endOffset, currentOffset);
                        if (currentOffset == nameOffset) {
                            return false;
                        }
                        if (currentOffset < endOffset) {
                            ch = data.charAt(currentOffset);
                        } else {
                            ch = -1;
                        }
                        nameHandle = symbolTable.addSymbol(data.substring(nameOffset, currentOffset));
                        if (ch != ':') {
                            prefixHandle = XMLSymbols.EMPTY_STRING;
                        } else {
                            prefixHandle = nameHandle;
                            if (++currentOffset == endOffset) {
                                return false;
                            }
                            nameOffset = currentOffset;
                            currentOffset = scanNCName(data, endOffset, currentOffset);
                            if (currentOffset == nameOffset) {
                                return false;
                            }
                            if (currentOffset < endOffset) {
                                ch = data.charAt(currentOffset);
                            } else {
                                ch = -1;
                            }
                            nameHandle = symbolTable.addSymbol(data.substring(nameOffset, currentOffset));
                        }
                        addToken(tokens, XPath.Tokens.EXPRTOKEN_VARIABLE_REFERENCE);
                        starIsMultiplyOperator = true;
                        tokens.addToken(prefixHandle);
                        tokens.addToken(nameHandle);
                        break;
                    case CHARTYPE_STAR:
                        if (starIsMultiplyOperator) {
                            addToken(tokens, XPath.Tokens.EXPRTOKEN_OPERATOR_MULT);
                            starIsMultiplyOperator = false;
                        } else {
                            addToken(tokens, XPath.Tokens.EXPRTOKEN_NAMETEST_ANY);
                            starIsMultiplyOperator = true;
                        }
                        if (++currentOffset == endOffset) {
                            break;
                        }
                        break;
                    case CHARTYPE_NONASCII:
                    case CHARTYPE_LETTER:
                    case CHARTYPE_UNDERSCORE:
                        nameOffset = currentOffset;
                        currentOffset = scanNCName(data, endOffset, currentOffset);
                        if (currentOffset == nameOffset) {
                            return false;
                        }
                        if (currentOffset < endOffset) {
                            ch = data.charAt(currentOffset);
                        } else {
                            ch = -1;
                        }
                        nameHandle = symbolTable.addSymbol(data.substring(nameOffset, currentOffset));
                        boolean isNameTestNCName = false;
                        boolean isAxisName = false;
                        prefixHandle = XMLSymbols.EMPTY_STRING;
                        if (ch == ':') {
                            if (++currentOffset == endOffset) {
                                return false;
                            }
                            ch = data.charAt(currentOffset);
                            if (ch == '*') {
                                if (++currentOffset < endOffset) {
                                    ch = data.charAt(currentOffset);
                                }
                                isNameTestNCName = true;
                            } else if (ch == ':') {
                                if (++currentOffset < endOffset) {
                                    ch = data.charAt(currentOffset);
                                }
                                isAxisName = true;
                            } else {
                                prefixHandle = nameHandle;
                                nameOffset = currentOffset;
                                currentOffset = scanNCName(data, endOffset, currentOffset);
                                if (currentOffset == nameOffset) {
                                    return false;
                                }
                                if (currentOffset < endOffset) {
                                    ch = data.charAt(currentOffset);
                                } else {
                                    ch = -1;
                                }
                                nameHandle = symbolTable.addSymbol(data.substring(nameOffset, currentOffset));
                            }
                        }
                        while (ch == ' ' || ch == 0x0A || ch == 0x09 || ch == 0x0D) {
                            if (++currentOffset == endOffset) {
                                break;
                            }
                            ch = data.charAt(currentOffset);
                        }
                        if (starIsMultiplyOperator) {
                            if (nameHandle == fAndSymbol) {
                                addToken(tokens, XPath.Tokens.EXPRTOKEN_OPERATOR_AND);
                                starIsMultiplyOperator = false;
                            } else if (nameHandle == fOrSymbol) {
                                addToken(tokens, XPath.Tokens.EXPRTOKEN_OPERATOR_OR);
                                starIsMultiplyOperator = false;
                            } else if (nameHandle == fModSymbol) {
                                addToken(tokens, XPath.Tokens.EXPRTOKEN_OPERATOR_MOD);
                                starIsMultiplyOperator = false;
                            } else if (nameHandle == fDivSymbol) {
                                addToken(tokens, XPath.Tokens.EXPRTOKEN_OPERATOR_DIV);
                                starIsMultiplyOperator = false;
                            } else {
                                return false;
                            }
                            if (isNameTestNCName) {
                                return false;
                            } else if (isAxisName) {
                                return false;
                            }
                            break;
                        }
                        if (ch == '(' && !isNameTestNCName && !isAxisName) {
                            if (nameHandle == fCommentSymbol) {
                                addToken(tokens, XPath.Tokens.EXPRTOKEN_NODETYPE_COMMENT);
                            } else if (nameHandle == fTextSymbol) {
                                addToken(tokens, XPath.Tokens.EXPRTOKEN_NODETYPE_TEXT);
                            } else if (nameHandle == fPISymbol) {
                                addToken(tokens, XPath.Tokens.EXPRTOKEN_NODETYPE_PI);
                            } else if (nameHandle == fNodeSymbol) {
                                addToken(tokens, XPath.Tokens.EXPRTOKEN_NODETYPE_NODE);
                            } else {
                                addToken(tokens, XPath.Tokens.EXPRTOKEN_FUNCTION_NAME);
                                tokens.addToken(prefixHandle);
                                tokens.addToken(nameHandle);
                            }
                            addToken(tokens, XPath.Tokens.EXPRTOKEN_OPEN_PAREN);
                            starIsMultiplyOperator = false;
                            if (++currentOffset == endOffset) {
                                break;
                            }
                            break;
                        }
                        if (isAxisName || (ch == ':' && currentOffset + 1 < endOffset && data.charAt(currentOffset + 1) == ':')) {
                            if (nameHandle == fAncestorSymbol) {
                                addToken(tokens, XPath.Tokens.EXPRTOKEN_AXISNAME_ANCESTOR);
                            } else if (nameHandle == fAncestorOrSelfSymbol) {
                                addToken(tokens, XPath.Tokens.EXPRTOKEN_AXISNAME_ANCESTOR_OR_SELF);
                            } else if (nameHandle == fAttributeSymbol) {
                                addToken(tokens, XPath.Tokens.EXPRTOKEN_AXISNAME_ATTRIBUTE);
                            } else if (nameHandle == fChildSymbol) {
                                addToken(tokens, XPath.Tokens.EXPRTOKEN_AXISNAME_CHILD);
                            } else if (nameHandle == fDescendantSymbol) {
                                addToken(tokens, XPath.Tokens.EXPRTOKEN_AXISNAME_DESCENDANT);
                            } else if (nameHandle == fDescendantOrSelfSymbol) {
                                addToken(tokens, XPath.Tokens.EXPRTOKEN_AXISNAME_DESCENDANT_OR_SELF);
                            } else if (nameHandle == fFollowingSymbol) {
                                addToken(tokens, XPath.Tokens.EXPRTOKEN_AXISNAME_FOLLOWING);
                            } else if (nameHandle == fFollowingSiblingSymbol) {
                                addToken(tokens, XPath.Tokens.EXPRTOKEN_AXISNAME_FOLLOWING_SIBLING);
                            } else if (nameHandle == fNamespaceSymbol) {
                                addToken(tokens, XPath.Tokens.EXPRTOKEN_AXISNAME_NAMESPACE);
                            } else if (nameHandle == fParentSymbol) {
                                addToken(tokens, XPath.Tokens.EXPRTOKEN_AXISNAME_PARENT);
                            } else if (nameHandle == fPrecedingSymbol) {
                                addToken(tokens, XPath.Tokens.EXPRTOKEN_AXISNAME_PRECEDING);
                            } else if (nameHandle == fPrecedingSiblingSymbol) {
                                addToken(tokens, XPath.Tokens.EXPRTOKEN_AXISNAME_PRECEDING_SIBLING);
                            } else if (nameHandle == fSelfSymbol) {
                                addToken(tokens, XPath.Tokens.EXPRTOKEN_AXISNAME_SELF);
                            } else {
                                return false;
                            }
                            if (isNameTestNCName) {
                                return false;
                            }
                            addToken(tokens, XPath.Tokens.EXPRTOKEN_DOUBLE_COLON);
                            starIsMultiplyOperator = false;
                            if (!isAxisName) {
                                currentOffset++;
                                if (++currentOffset == endOffset) {
                                    break;
                                }
                            }
                            break;
                        }
                        if (isNameTestNCName) {
                            addToken(tokens, XPath.Tokens.EXPRTOKEN_NAMETEST_NAMESPACE);
                            starIsMultiplyOperator = true;
                            tokens.addToken(nameHandle);
                        } else {
                            addToken(tokens, XPath.Tokens.EXPRTOKEN_NAMETEST_QNAME);
                            starIsMultiplyOperator = true;
                            tokens.addToken(prefixHandle);
                            tokens.addToken(nameHandle);
                        }
                        break;
                }
            }
            if (XPath.Tokens.DUMP_TOKENS) {
                tokens.dumpTokens();
            }
            return true;
        }

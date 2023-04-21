    protected void transformNode(SSATree tree, boolean decl, ParseTreeNode node, List<SSATreeNode> nodes) throws SemanticException {
        SSATreeNode ssaNode;
        int stmt = node.getStatementType();
        SSASymbolTable table = tree.getSymbolTable();
        List<SSATreeNode> children;
        CodeLocation loc = _gl(node);
        switch(stmt) {
            case PRECEDENCE_STMT:
            case DISCARD_STMT:
                transformNode(tree, decl, node.getSubNode(0), nodes);
                break;
            case VARIABLE:
            case ASSIGNMENT:
            case ASSIGNMENT_POP:
                ScriptSymbol sym = node.getSymbol();
                if (!table.has(sym.getName(), loc)) {
                    if (!decl) {
                        throw new SemanticException("Undeclared variable " + sym.getName() + " at line " + loc.lineno + " in " + loc.fname);
                    }
                    table.declare(sym.getType(), sym.getName(), loc);
                }
                if (stmt == VARIABLE) {
                    break;
                }
            case PLUS_EXPR:
            case MINUS_EXPR:
            case B_AND_EXPR:
            case B_OR_EXPR:
            case B_XOR_EXPR:
            case L_AND_EXPR:
            case L_OR_EXPR:
            case MULTIPLY_EXPR:
            case DIVIDE_EXPR:
            case MODULUS_EXPR:
            case RET_STMT:
            case SIGN_INVERT:
            case LESS_THAN:
            case LESS_EQUAL:
            case GREATER_THAN:
            case GREATER_EQUAL:
            case EQUAL:
            case NOT_EQUAL:
            case BIT_COMPLEMENT:
            case PRE_INCREMENT:
            case PRE_DECREMENT:
            case CONCAT_EXPR:
                createOpNode(tree, decl, pts2ssa(stmt), node, nodes);
                break;
            case POST_INCREMENT:
            case POST_DECREMENT:
                int type = stmt == POST_INCREMENT ? ADD : SUB;
                String varname = node.getSubNode(0).getSymbol().getName();
                Type t = node.getReturnType();
                SSAVariable v;
                v = table.read(varname, loc);
                nodes.add(_sl(tree.createOpNode(type, table.write(varname, loc), v, new SSAConstant(t, 1)), loc));
                nodes.add(_sl(tree.createOpNode(ASSIGN, table.makeTemp(t), v), loc));
                break;
            case DECLARE_LIST:
                if (decl) {
                    throw new AssertionError("Invalid parse tree");
                }
                decl = true;
            case COMMA_LIST:
            case STMT_LIST:
            case CALL_FUNC:
            case PRINT_STMT:
            case REF_CALL:
            case NEW_REF:
                final int cc = node.getSubNodeCount();
                SSAValue vars[] = null;
                if (stmt == STMT_LIST) {
                    table.pushScope();
                    children = new ArrayList<SSATreeNode>();
                } else {
                    children = nodes;
                    if (stmt == CALL_FUNC || stmt == PRINT_STMT || stmt == REF_CALL || stmt == NEW_REF) {
                        vars = new SSAValue[cc + (stmt == REF_CALL ? 1 : 0)];
                    }
                }
                for (int i = 0; i < cc; i++) {
                    ParseTreeNode sub = node.getSubNode(i);
                    if (isLeaf(sub)) {
                        SSAValue val;
                        SSATreeNode n;
                        if (stmt != CALL_FUNC && stmt != PRINT_STMT && stmt != REF_CALL && stmt != NEW_REF) {
                            throw new AssertionError("Should not meet a leaf node here");
                        }
                        val = convertLeaf(table, sub);
                        n = _sl(tree.createOpNode(val.isConst() ? PUSH : LOAD, table.makeTemp(val.getType()), val), loc);
                        nodes.add(n);
                        vars[i] = n.getAssign();
                        continue;
                    }
                    transformNode(tree, decl, sub, children);
                    if (stmt == CALL_FUNC || stmt == PRINT_STMT || stmt == REF_CALL) {
                        SSATreeNode l = children.get(children.size() - 1);
                        SSAVariable la = l.getAssign();
                        if (!table.isTemp(la)) {
                            l = _sl(tree.createOpNode(LOAD, table.makeTemp(la.getType()), la), loc);
                            nodes.add(l);
                            la = l.getAssign();
                        }
                        vars[i] = la;
                    }
                }
                if (stmt == STMT_LIST) {
                    SSAVariable[] svars = table.popScope();
                    ssaNode = tree.createScopeNode(children);
                    _sl(ssaNode, loc);
                    nodes.add(ssaNode);
                    for (SSAVariable var : svars) {
                        table.bump(var);
                    }
                } else if (stmt == CALL_FUNC || stmt == PRINT_STMT || stmt == REF_CALL || stmt == NEW_REF) {
                    FunctionSymbol func = (FunctionSymbol) node.getSymbol();
                    int ssaType;
                    switch(stmt) {
                        case CALL_FUNC:
                        case PRINT_STMT:
                            ssaType = CALL;
                            break;
                        case NEW_REF:
                            ssaType = NEW;
                            break;
                        case REF_CALL:
                            ssaType = RCALL;
                            break;
                        default:
                            throw new AssertionError();
                    }
                    ssaNode = _sl(tree.createOpNode(ssaType, table.makeTemp(node.getReturnType()), vars), loc);
                    if (stmt == REF_CALL) {
                        vars[vars.length - 1] = table.read(func.getName(), loc);
                    } else {
                        ssaNode.setFunc(func);
                    }
                    nodes.add(ssaNode);
                }
                break;
            case IF_STMT:
            case IFELSE_STMT:
                SSAValue ifcond;
                boolean hasElse = node.getSubNodeCount() > 2;
                List<SSATreeNode> ifNodes = new ArrayList<SSATreeNode>();
                List<SSATreeNode> elseNodes = new ArrayList<SSATreeNode>();
                SSAVariable[] ifVars;
                SSAVariable[] elseVars;
                SSATreeNode ifNode;
                Map<String, SSAVariable> vmap = new HashMap<String, SSAVariable>();
                if (isLeaf(node.getSubNode(0))) {
                    ifcond = convertLeaf(table, node.getSubNode(0));
                } else {
                    transformNode(tree, decl, node.getSubNode(0), nodes);
                    ifcond = nodes.get(nodes.size() - 1).getAssign();
                }
                table.pushScope();
                transformNode(tree, decl, node.getSubNode(1), ifNodes);
                ifVars = table.popScope();
                if (hasElse) {
                    table.pushScope();
                    transformNode(tree, decl, node.getSubNode(2), elseNodes);
                    elseVars = table.popScope();
                    for (SSAVariable var : elseVars) {
                        String name = table.getBasename(var);
                        vmap.put(name, var);
                    }
                }
                ifNode = _sl(tree.createOpNode(IFELSE, table.makeTemp(new Type(SymbolType.SYM_VOID)), ifcond), loc);
                ifNode.setChildren(new SSATreeNode[] { _sl(tree.createScopeNode(SCOPE, ifNodes), loc), _sl(tree.createScopeNode(SCOPE, elseNodes), loc) });
                nodes.add(ifNode);
                for (SSAVariable var : ifVars) {
                    String name = table.getBasename(var);
                    SSAVariable old = hasElse ? vmap.remove(name) : null;
                    if (old == null) {
                        old = table.read(name, loc);
                    }
                    nodes.add(_sl(tree.createOpNode(PHI, table.write(name, loc), var, old), loc));
                }
                for (SSAVariable var : vmap.values()) {
                    String name = table.getBasename(var);
                    SSAVariable old = table.read(name, loc);
                    nodes.add(_sl(tree.createOpNode(PHI, table.write(name, loc), var, old), loc));
                }
                break;
            case FOR_IMP_ITER:
                List<SSATreeNode> init = new ArrayList<SSATreeNode>();
                List<SSATreeNode> cond = new ArrayList<SSATreeNode>();
                List<SSATreeNode> inc = new ArrayList<SSATreeNode>();
                List<SSATreeNode> body = new ArrayList<SSATreeNode>();
                table = tree.getSymbolTable();
                table.pushScope();
                transformNode(tree, decl, node.getSubNode(0), init);
                transformNode(tree, decl, node.getSubNode(1), cond);
                transformNode(tree, decl, node.getSubNode(2), inc);
                transformNode(tree, decl, node.getSubNode(3), body);
                nodes.add(_sl(tree.createScopeNode(LOOP, new SSATreeNode[] { _sl(tree.createScopeNode(LOOP_INIT, init), node.getSubNode(0)), _sl(tree.createScopeNode(LOOP_COND, cond), node.getSubNode(1)), _sl(tree.createScopeNode(LOOP_INC, inc), node.getSubNode(2)), _sl(tree.createScopeNode(LOOP_BODY, body), node.getSubNode(3)) }), loc));
                vars = table.popScope();
                for (SSAVariable var : (SSAVariable[]) vars) {
                    String name = table.getBasename(var);
                    SSAVariable old = table.read(name, loc);
                    nodes.add(tree.createOpNode(PHI, table.write(name, loc), var, old));
                }
                break;
            case ASSEMBLER:
                SSAValue[] asm = new SSAValue[node.getSubNodeCount()];
                Pattern varPat = Pattern.compile("\\$\\{([^}]+)\\}");
                Map<String, Boolean> reads = new HashMap<String, Boolean>();
                Map<String, Boolean> writes = new HashMap<String, Boolean>();
                SSAVariable[] r, w;
                int i;
                table = tree.getSymbolTable();
                for (i = 0; i < asm.length; i++) {
                    String a = (String) node.getSubNode(i).getSymbol().getValue();
                    Matcher ma = varPat.matcher(a);
                    boolean store = false;
                    if (a.matches("\\S+store.*") || a.matches("^iinc.*")) {
                        store = true;
                    }
                    while (ma.find()) {
                        for (int j = 1; j <= ma.groupCount(); j++) {
                            String varName = ma.group(j);
                            Map<String, Boolean> map = store ? writes : reads;
                            if (map.get(varName) != null) {
                                continue;
                            }
                            map.put(varName, Boolean.TRUE);
                        }
                    }
                    asm[i] = new SSAConstant(new Type(SymbolType.SYM_ASM_INST), a);
                }
                ssaNode = tree.createOpNode(ASM, null, asm);
                nodes.add(ssaNode);
                r = new SSAVariable[reads.size()];
                w = new SSAVariable[writes.size()];
                i = 0;
                for (String n : reads.keySet()) {
                    r[i++] = table.read(n, loc);
                }
                i = 0;
                for (String n : writes.keySet()) {
                    SSAVariable cur = table.read(n, loc);
                    w[i] = table.write(n, loc);
                    nodes.add(_sl(tree.createOpNode(PHI, table.write(n, loc), cur, w[i]), loc));
                    ++i;
                }
                ssaNode.setAsmReads(r);
                ssaNode.setAsmWrites(w);
                _sl(ssaNode, loc);
                break;
            case EMPTY_STMT:
                break;
            default:
                throw new IllegalArgumentException("Unsupport node " + statementNames[stmt] + " (" + node.getFilename() + ":" + node.getLineno() + ")");
        }
    }

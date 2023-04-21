    public void visitSwitch(JCSwitch tree) {
        int limit = code.nextreg;
        Assert.check(tree.selector.type.tag != CLASS);
        int startpcCrt = genCrt ? code.curPc() : 0;
        Item sel = genExpr(tree.selector, syms.intType);
        List<JCCase> cases = tree.cases;
        if (cases.isEmpty()) {
            sel.load().drop();
            if (genCrt) code.crt.put(TreeInfo.skipParens(tree.selector), CRT_FLOW_CONTROLLER, startpcCrt, code.curPc());
        } else {
            sel.load();
            if (genCrt) code.crt.put(TreeInfo.skipParens(tree.selector), CRT_FLOW_CONTROLLER, startpcCrt, code.curPc());
            Env<GenContext> switchEnv = env.dup(tree, new GenContext());
            switchEnv.info.isSwitch = true;
            int lo = Integer.MAX_VALUE;
            int hi = Integer.MIN_VALUE;
            int nlabels = 0;
            int[] labels = new int[cases.length()];
            int defaultIndex = -1;
            List<JCCase> l = cases;
            for (int i = 0; i < labels.length; i++) {
                if (l.head.pat != null) {
                    int val = ((Number) l.head.pat.type.constValue()).intValue();
                    labels[i] = val;
                    if (val < lo) lo = val;
                    if (hi < val) hi = val;
                    nlabels++;
                } else {
                    Assert.check(defaultIndex == -1);
                    defaultIndex = i;
                }
                l = l.tail;
            }
            long table_space_cost = 4 + ((long) hi - lo + 1);
            long table_time_cost = 3;
            long lookup_space_cost = 3 + 2 * (long) nlabels;
            long lookup_time_cost = nlabels;
            int opcode = nlabels > 0 && table_space_cost + 3 * table_time_cost <= lookup_space_cost + 3 * lookup_time_cost ? tableswitch : lookupswitch;
            int startpc = code.curPc();
            code.emitop0(opcode);
            code.align(4);
            int tableBase = code.curPc();
            int[] offsets = null;
            code.emit4(-1);
            if (opcode == tableswitch) {
                code.emit4(lo);
                code.emit4(hi);
                for (long i = lo; i <= hi; i++) {
                    code.emit4(-1);
                }
            } else {
                code.emit4(nlabels);
                for (int i = 0; i < nlabels; i++) {
                    code.emit4(-1);
                    code.emit4(-1);
                }
                offsets = new int[labels.length];
            }
            Code.State stateSwitch = code.state.dup();
            code.markDead();
            l = cases;
            for (int i = 0; i < labels.length; i++) {
                JCCase c = l.head;
                l = l.tail;
                int pc = code.entryPoint(stateSwitch);
                if (i != defaultIndex) {
                    if (opcode == tableswitch) {
                        code.put4(tableBase + 4 * (labels[i] - lo + 3), pc - startpc);
                    } else {
                        offsets[i] = pc - startpc;
                    }
                } else {
                    code.put4(tableBase, pc - startpc);
                }
                genStats(c.stats, switchEnv, CRT_FLOW_TARGET);
            }
            code.resolve(switchEnv.info.exit);
            if (code.get4(tableBase) == -1) {
                code.put4(tableBase, code.entryPoint(stateSwitch) - startpc);
            }
            if (opcode == tableswitch) {
                int defaultOffset = code.get4(tableBase);
                for (long i = lo; i <= hi; i++) {
                    int t = (int) (tableBase + 4 * (i - lo + 3));
                    if (code.get4(t) == -1) code.put4(t, defaultOffset);
                }
            } else {
                if (defaultIndex >= 0) for (int i = defaultIndex; i < labels.length - 1; i++) {
                    labels[i] = labels[i + 1];
                    offsets[i] = offsets[i + 1];
                }
                if (nlabels > 0) qsort2(labels, offsets, 0, nlabels - 1);
                for (int i = 0; i < nlabels; i++) {
                    int caseidx = tableBase + 8 * (i + 1);
                    code.put4(caseidx, labels[i]);
                    code.put4(caseidx + 4, offsets[i]);
                }
            }
        }
        code.endScopes(limit);
    }

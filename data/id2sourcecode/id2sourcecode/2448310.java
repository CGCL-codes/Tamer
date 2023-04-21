    private void computeNesting(Expression e, MethodScope root) {
        for (Expression f = e.getFirstExpression(); f != null; f = f.getNextExpression()) {
            if (f instanceof XMethod) {
                MethodScope ms = MethodScope.get(f);
                ms.nesting = nesting + 1;
                ms.computeNesting(f, root);
            } else {
                computeNesting(f, root);
            }
        }
        if (e instanceof ControlTransfer) {
            ControlTransfer ct = (ControlTransfer) e;
            MethodScope targetScope;
            if (ct instanceof Break) {
                targetScope = MethodScope.get(root.getTargetFor((Break) ct));
            } else if (ct instanceof Return) {
                targetScope = ((Return) ct).getScope();
            } else {
                throw new AssertionError(ct);
            }
            ct.setNesting(nesting - targetScope.nesting);
            MethodScope m = this;
            if (m != targetScope) {
                while (m != targetScope) {
                    e = (Expression) m.getMethod().getAxisParent();
                    m = MethodScope.get(m.getEnclosingScope());
                }
                ((NonlocalGenerator) e).addTransfer(ct);
            }
            if (ct instanceof Return) {
                while (e != m.getBlock()) {
                    if (e.needsEmptyOperandStackForFinally()) {
                        m.declareResultLocal();
                        break;
                    }
                    e = (Expression) e.getAxisParent();
                }
            }
        } else if (e instanceof LocalAccess) {
            LocalAccess a = (LocalAccess) e;
            for (int i = a.getLocalCount() - 1; i >= 0; i--) {
                Local l = a.getLocal(i);
                if ((l != null) && (l.methodScope != this)) {
                    if (l.isJavaLocal()) {
                        l = l.methodScope.makeVMXLocal(l);
                    }
                    Local n;
                    if (nestedLocals == null) {
                        nestedLocals = new HashMap<Local, Local>();
                        n = null;
                    } else {
                        n = nestedLocals.get(l);
                    }
                    if (n == null) {
                        n = declareLocal(l.getName(), 0, l.getType(), l.getAST());
                        n.ref = l;
                        n.nesting = nesting - l.methodScope.nesting;
                        nestedLocals.put(l, n);
                    }
                    a.setLocal(i, n);
                }
            }
        }
    }

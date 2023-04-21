    public void generateInternalInterfaceDef(JCEventDecl tree, int pos) {
        ListBuffer<JCTree> defs = new ListBuffer<JCTree>();
        JCModifiers publicMods = mods(Flags.PUBLIC);
        JCModifiers publicSyntheticMods = mods(Flags.PUBLIC);
        JCModifiers privateMods = mods(Flags.PRIVATE);
        JCModifiers noMods = mods(0);
        JCModifiers publicInterfaceMods = mods(Flags.PUBLIC | Flags.INTERFACE);
        JCModifiers publicFinalMods = mods(Flags.PUBLIC | Flags.FINAL);
        JCModifiers publicStaticMods = mods(Flags.PUBLIC | Flags.STATIC);
        JCModifiers privateStaticMods = mods(Flags.PRIVATE | Flags.STATIC);
        JCModifiers privateStaticFinalMods = mods(Flags.PRIVATE | Flags.STATIC | Flags.FINAL);
        int i = 1;
        for (JCTree c : tree.getContextVariables()) {
            JCVariableDecl contextVariable = (JCVariableDecl) c;
            JCModifiers contextVariableMods = mods(Flags.PUBLIC, lb(ann(PtolemyConstants.CONTEXT_VARIABLE_ANN_TYPE_NAME, args(assign("index", intc(i))))));
            defs.append(method(contextVariableMods, contextVariable.getName().toString(), (JCExpression) contextVariable.getType()));
            i++;
        }
        defs.append(method(publicSyntheticMods, PtolemyConstants.INVOKE_METHOD_NAME, tree.getReturnType(), params(), throwing(id("Throwable"))));
        JCModifiers registerMods = mods(Flags.PUBLIC | Flags.STATIC | Flags.SYNCHRONIZED, lb(ann(PtolemyConstants.IGNORE_REFINEMENT_CHECKING_ANN_TYPE_NAME)));
        JCModifiers handlersChangedMods = mods(Flags.PRIVATE | Flags.STATIC | Flags.SYNCHRONIZED, lb(ann(PtolemyConstants.IGNORE_REFINEMENT_CHECKING_ANN_TYPE_NAME)));
        JCModifiers announceMods = mods(Flags.PUBLIC | Flags.STATIC, lb(ann(PtolemyConstants.IGNORE_REFINEMENT_CHECKING_ANN_TYPE_NAME)));
        defs.append(clazz(publicInterfaceMods, PtolemyConstants.EVENT_HANDLER_IFACE_NAME, defs(method(publicSyntheticMods, PtolemyConstants.EVENT_HANDLER_METHOD_NAME, tree.getReturnType(), params(var(noMods, "next", id(tree.getSimpleName()))), throwing(id("Throwable"))))));
        ListBuffer<JCTree> eventFrameDefs = defs(var(publicStaticMods, PtolemyConstants.HANDLERS_LIST_NAME, ta(select("java.util.List"), typeargs(id(PtolemyConstants.EVENT_HANDLER_IFACE_NAME)))), var(publicStaticMods, "cachedHandlerRecords", PtolemyConstants.EVENT_FRAME_TYPE_NAME), method(handlersChangedMods, PtolemyConstants.HANDLERS_CHANGED_METHOD_NAME, voidt(), params(), body(var(noMods, "i", select("java.util.Iterator"), apply(PtolemyConstants.HANDLERS_LIST_NAME, "iterator")), var(noMods, "newRecords", PtolemyConstants.EVENT_FRAME_TYPE_NAME, newt(PtolemyConstants.EVENT_FRAME_TYPE_NAME, args(id("i")))), es(apply("recordWriteLock", "lock")), tryt(body(es(assign("cachedHandlerRecords", id("newRecords")))), body(es(apply("recordWriteLock", "unlock")))))), method(registerMods, PtolemyConstants.REGISTER_METHOD_NAME, PtolemyConstants.EVENT_HANDLER_IFACE_NAME, params(var(noMods, "h", PtolemyConstants.EVENT_HANDLER_IFACE_NAME)), body(ift(isNull(PtolemyConstants.HANDLERS_LIST_NAME), es(assign(PtolemyConstants.HANDLERS_LIST_NAME, apply("java.util.Collections", "synchronizedList", args(newt(ta(select("java.util.ArrayList"), typeargs(id(PtolemyConstants.EVENT_HANDLER_IFACE_NAME))), args())))))), es(apply(PtolemyConstants.HANDLERS_LIST_NAME, "add", args(id("h")))), es(apply(PtolemyConstants.HANDLERS_CHANGED_METHOD_NAME)), returnt(id("h")))), method(registerMods, PtolemyConstants.UNREGISTER_METHOD_NAME, PtolemyConstants.EVENT_HANDLER_IFACE_NAME, params(var(noMods, "h", PtolemyConstants.EVENT_HANDLER_IFACE_NAME)), body(ift(isNull(PtolemyConstants.HANDLERS_LIST_NAME), returnt(id("h"))), ift(nott(apply(PtolemyConstants.HANDLERS_LIST_NAME, "contains", args(id("h")))), returnt(id("h"))), es(apply(PtolemyConstants.HANDLERS_LIST_NAME, "remove", args(apply(PtolemyConstants.HANDLERS_LIST_NAME, "lastIndexOf", args(id("h")))))), ift(apply(PtolemyConstants.HANDLERS_LIST_NAME, "isEmpty"), body(es(assign("cachedHandlerRecords", nullt())), es(assign(PtolemyConstants.HANDLERS_LIST_NAME, nullt()))), es(apply(PtolemyConstants.HANDLERS_CHANGED_METHOD_NAME))), returnt(id("h")))), var(privateStaticMods, "body", id(tree.getSimpleName())), method(announceMods, PtolemyConstants.ANNOUNCE_METHOD_NAME, tree.getReturnType(), params(var(noMods, "ev", id(tree.getSimpleName()))), throwing(id("Throwable")), body(var(noMods, "record", PtolemyConstants.EVENT_FRAME_TYPE_NAME), es(apply("recordReadLock", "lock")), tryt(body(es(assign("record", id("cachedHandlerRecords")))), body(es(apply("recordReadLock", "unlock")))), ift(notNull(select("record.nextRecord")), es(assign(select("EventFrame.body"), id("ev")))), isVoid(tree.getReturnType()) ? es(apply("record.handler", PtolemyConstants.EVENT_HANDLER_METHOD_NAME, args(id("record")))) : returnt(apply("record.handler", PtolemyConstants.EVENT_HANDLER_METHOD_NAME, args(id("record")))))), method(publicMods, PtolemyConstants.INVOKE_METHOD_NAME, tree.getReturnType(), params(), throwing(id("Throwable")), body(ift(notNull(select("nextRecord.handler")), isVoid(tree.getReturnType()) ? body(es(apply("nextRecord.handler", PtolemyConstants.EVENT_HANDLER_METHOD_NAME, args(id("nextRecord")))), returnt()) : returnt(apply("nextRecord.handler", PtolemyConstants.EVENT_HANDLER_METHOD_NAME, args(id("nextRecord"))))), isVoid(tree.getReturnType()) ? es(apply("EventFrame.body", PtolemyConstants.INVOKE_METHOD_NAME)) : returnt(apply("EventFrame.body", PtolemyConstants.INVOKE_METHOD_NAME)))), var(privateMods, "handler", PtolemyConstants.EVENT_HANDLER_IFACE_NAME), var(privateMods, "nextRecord", PtolemyConstants.EVENT_FRAME_TYPE_NAME), constructor(privateMods, params(var(noMods, "chain", select("java.util.Iterator"))), body(es(supert()), ift(apply("chain", "hasNext"), body(es(assign("handler", cast(PtolemyConstants.EVENT_HANDLER_IFACE_NAME, apply("chain", "next")))), es(assign("nextRecord", newt(PtolemyConstants.EVENT_FRAME_TYPE_NAME, args(id("chain"))))), returnt())), es(assign("nextRecord", nullt())))), var(privateStaticFinalMods, "recordLock", select("java.util.concurrent.locks.ReentrantReadWriteLock"), newt(select("java.util.concurrent.locks.ReentrantReadWriteLock"))), var(privateStaticFinalMods, "recordReadLock", select("java.util.concurrent.locks.Lock"), apply("recordLock", "readLock")), var(privateStaticFinalMods, "recordWriteLock", select("java.util.concurrent.locks.Lock"), apply("recordLock", "writeLock")));
        for (JCTree c : tree.getContextVariables()) {
            JCVariableDecl contextVariable = (JCVariableDecl) c;
            JCModifiers contextVariableMods = mods(Flags.PUBLIC);
            eventFrameDefs.append(method(contextVariableMods, contextVariable.getName(), (JCExpression) contextVariable.getType(), body(returnt(apply("body", contextVariable.getName().toString())))));
        }
        defs.append(clazz(publicFinalMods, PtolemyConstants.EVENT_FRAME_TYPE_NAME, implementing(id(tree.getSimpleName())), eventFrameDefs));
        ListBuffer<JCTree> eventClosureDefs = new ListBuffer<JCTree>();
        ListBuffer<JCVariableDecl> constructorParams = new ListBuffer<JCVariableDecl>();
        ListBuffer<JCStatement> constructorBodyStatements = new ListBuffer<JCStatement>();
        for (JCTree c : tree.getContextVariables()) {
            JCVariableDecl contextVariable = (JCVariableDecl) c;
            JCModifiers contextVariableMods = mods(Flags.PUBLIC | Flags.FINAL);
            eventClosureDefs.append(var(privateMods, contextVariable.getName().toString(), (JCExpression) contextVariable.getType()));
            eventClosureDefs.append(method(contextVariableMods, contextVariable.getName(), (JCExpression) contextVariable.getType(), body(returnt(id(contextVariable.getName().toString())))));
            constructorParams.append(var(noMods, contextVariable.getName().toString(), (JCExpression) contextVariable.getType()));
            constructorBodyStatements.append(es(assign(select(thist(), contextVariable.getName().toString()), id(contextVariable.getName()))));
        }
        eventClosureDefs.append(method(publicMods, PtolemyConstants.INVOKE_METHOD_NAME, tree.getReturnType(), params(), isVoid(tree.getReturnType()) ? body() : body(returnt(defaultt(tree.getReturnType())))));
        eventClosureDefs.append(constructor(publicMods, constructorParams, body(constructorBodyStatements)));
        defs.append(clazz(publicStaticMods, PtolemyConstants.EVENT_CLOSURE_TYPE_NAME, implementing(id(tree.getSimpleName())), eventClosureDefs));
        JCModifiers internalMods = mods(tree.getModifiers().flags | Flags.INTERFACE, lb(ann(PtolemyConstants.EVENT_TYPE_DECL_ANN_TYPE_NAME), ann(PtolemyConstants.EVENT_CONTRACT_DECL_ANN_TYPE_NAME, args(assign("assumesBlock", tree.contract == null ? stringc("null") : stringc(tree.contract.assumesBlock.toString()))))));
        tree.mods = internalMods;
        tree.defs = defs.toList();
    }

    public OPT_InlineDecision shouldInline(OPT_CompilationState state) {
        if (!state.getOptions().INLINE) {
            return OPT_InlineDecision.NO("inlining not enabled");
        }
        VM_Method caller = state.getMethod();
        VM_Method callee = state.obtainTarget();
        int inlinedSizeEstimate = 0;
        if (!state.isInvokeInterface()) {
            if (!legalToInline(caller, callee)) return OPT_InlineDecision.NO("illegal inlining");
            if (OPT_InlineTools.hasInlinePragma(callee, state)) return OPT_InlineDecision.YES(callee, "pragmaInline");
            if (OPT_InlineTools.hasNoInlinePragma(callee, state)) return OPT_InlineDecision.NO("pragmaNoInline");
            inlinedSizeEstimate = OPT_InlineTools.inlinedSizeEstimate(callee, state);
            if (inlinedSizeEstimate < state.getOptions().IC_MAX_ALWAYS_INLINE_TARGET_SIZE && (!needsGuard(callee) || state.getComputedTarget() != null) && !state.getSequence().containsMethod(callee)) {
                return OPT_InlineDecision.YES(callee, "trivial inline");
            }
        }
        return shouldInlineInternal(caller, callee, state, inlinedSizeEstimate);
    }

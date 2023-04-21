    public static void unparseIsWrite(IRNode node, JavaUnparser u) {
        (isWrite(node) ? writeToken : readToken).emit(u, node);
    }

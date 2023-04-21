    @Override
    public Object startElement(String name, Attributes attr, Object current, Object placeholder) throws Exception {
        if (placeholder != null) {
            return this.startExpression((SelectablePlaceholder<?>) placeholder, name, attr);
        } else if (current == null) {
            return this.readMainObject(name, attr);
        } else if (current instanceof MathObject) {
            return this.readMathObject(name, attr, (MathObject) current);
        } else if (current instanceof Constructor.EqualitySpecification) {
            return this.readEqualitySpecification(name, attr, (Constructor.EqualitySpecification) current);
        } else if (current instanceof Constructor.RewriteRule) {
            return this.readRewriteRule(name, attr, (Constructor.RewriteRule) current);
        } else if (current instanceof Construction.Embedding) {
            return this.readEmbedding(name, attr, (Construction.Embedding) current);
        } else if (current instanceof Operator.ImplicitDefinition) {
            return this.readImplicitDefinition(name, attr, (Operator.ImplicitDefinition) current);
        } else if (current instanceof Theorem.Contents) {
            return this.readTheoremContents(name, attr, (Theorem.Contents) current);
        } else if (current instanceof ParameterList) {
            return this.readParameterList(name, attr, (ParameterList) current);
        } else if (current instanceof Parameter) {
            return this.readParameter(name, attr, (Parameter) current);
        } else if (current instanceof ElementParameter.ShortCut) {
            return this.readShortCut(name, attr, (ElementParameter.ShortCut) current);
        } else if (current instanceof ArgumentList) {
            return this.readArgumentList(name, attr, (ArgumentList) current);
        } else if (current instanceof Argument) {
            return this.readArgument(name, attr, (Argument) current);
        } else if (current instanceof MathObjectReference<?>) {
            return this.readMathObjectReference(name, attr, (MathObjectReference<?>) current);
        } else if (current instanceof Formula) {
            return this.readFormula(name, attr, (Formula) current);
        } else if (current instanceof SetTerm) {
            return this.readSetTerm(name, attr, (SetTerm) current);
        } else if (current instanceof ElementTerm) {
            return this.readElementTerm(name, attr, (ElementTerm) current);
        } else if (current instanceof SymbolTerm) {
            return this.readSymbolTerm(name, attr, (SymbolTerm) current);
        } else if (current instanceof ContextItemReference<?>) {
            return this.readContextItemReference(name, attr, (ContextItemReference<?>) current);
        } else if (current instanceof ContextItemReference.Binding) {
            return this.readBinding(name, attr, (ContextItemReference.Binding) current);
        } else if (current instanceof StructuralCaseList<?>) {
            return this.readStructuralCaseList(name, attr, (StructuralCaseList<?>) current);
        } else if (current instanceof StructuralCaseList.Case<?>) {
            return this.readStructuralCase(name, attr, (StructuralCaseList.Case<?>) current);
        } else if (current instanceof EquivalenceList<?>) {
            return this.readEquivalenceList(name, attr, (EquivalenceList<?>) current);
        } else if (current instanceof ProofWithParameters) {
            return this.readProofWithParameters(name, attr, (ProofWithParameters) current);
        } else if (current instanceof Proof) {
            return this.readProof(name, attr, (Proof) current);
        } else if (current instanceof ProofStep) {
            return this.readProofStep(name, attr, (ProofStep) current);
        }
        return null;
    }

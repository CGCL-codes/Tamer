    @Override
    public void process(ITranslationContext context, ASTNode node, TranslatorASTRewrite rew, TranslatorASTRewrite subRewriter, TextEditGroup description) {
        if (context.getConfiguration().getOptions().getGlobalOptions().isPerfCount()) {
            final String tracked = node.toString();
            if (visited.contains(node.getNodeType() + tracked + node.getParent().toString())) {
                context.getLogger().logInfo("Field " + tracked + " already modified by FieldRewriter/" + description.getName());
            } else {
                visited.add(node.getNodeType() + tracked + node.getParent().toString());
            }
        }
        switch(node.getNodeType()) {
            case ASTNode.FIELD_ACCESS:
                {
                    processFieldAccess(context, node, rew);
                    break;
                }
            case ASTNode.QUALIFIED_NAME:
                {
                    processQualifiedName(context, node, rew);
                    break;
                }
            case ASTNode.SIMPLE_NAME:
                {
                    processSimpleName(context, node, rew);
                    break;
                }
            case ASTNode.FIELD_DECLARATION:
                {
                    processFieldDeclaration(context, node, rew, subRewriter, description);
                    break;
                }
            case ASTNode.VARIABLE_DECLARATION_FRAGMENT:
                {
                    processVariableDeclarationFragment(context, node, rew, subRewriter, description);
                    break;
                }
            case ASTNode.SINGLE_VARIABLE_DECLARATION:
                {
                    processSingleVariableDeclaration(context, node, rew, subRewriter, description);
                    break;
                }
            case ASTNode.VARIABLE_DECLARATION_EXPRESSION:
                {
                    processVariableDeclarationExpression(context, node, rew);
                    break;
                }
            case ASTNode.VARIABLE_DECLARATION_STATEMENT:
                {
                    processVariableDeclarationStatement(context, node, rew, subRewriter, description);
                    break;
                }
        }
    }

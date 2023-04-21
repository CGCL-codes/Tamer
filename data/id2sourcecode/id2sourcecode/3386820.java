    private FieldDeclaration builEnumField(AST a_ast) {
        VariableDeclarationFragment l_frag = a_ast.newVariableDeclarationFragment();
        l_frag.setName(a_ast.newSimpleName(this._enumField));
        FieldDeclaration l_field = a_ast.newFieldDeclaration(l_frag);
        l_field.setType(a_ast.newPrimitiveType(PrimitiveType.INT));
        l_field.modifiers().add(a_ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
        l_field.modifiers().add(a_ast.newModifier(ModifierKeyword.STATIC_KEYWORD));
        currentRewriter.replace(l_field.getType(), currentRewriter.createStringPlaceholder("readonly " + l_field.getType().toString(), l_field.getType().getNodeType()), description);
        return l_field;
    }

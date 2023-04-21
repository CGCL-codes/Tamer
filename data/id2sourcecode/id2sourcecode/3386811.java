    private void addConstants(TypeDeclaration a_type, List<EnumConstantDeclaration> a_constants) {
        AST l_ast = a_type.getAST();
        int l_cpt = 0;
        for (EnumConstantDeclaration l_constant : a_constants) {
            VariableDeclarationFragment l_frag = l_ast.newVariableDeclarationFragment();
            ClassInstanceCreation l_classInstCrea = l_ast.newClassInstanceCreation();
            l_classInstCrea.setType(l_ast.newSimpleType(l_ast.newSimpleName(a_type.getName().toString())));
            List<Expression> l_constArgs = l_constant.arguments();
            for (Expression l_t : l_constArgs) {
                Expression l_expr = (Expression) ASTNode.copySubtree(l_ast, l_t);
                if (l_expr.toString().endsWith(".class")) {
                    ASTNode l_typeof = currentRewriter.createStringPlaceholder("/* typeof(" + l_expr.toString().replace(".class", "") + ") */" + l_expr.toString(), ASTNode.SIMPLE_NAME);
                    l_classInstCrea.arguments().add(l_typeof);
                } else {
                    l_classInstCrea.arguments().add(l_expr);
                }
            }
            StringLiteral l_constantName = l_ast.newStringLiteral();
            l_constantName.setLiteralValue(l_constant.getName().toString());
            l_classInstCrea.arguments().add(l_constantName);
            l_classInstCrea.arguments().add(l_ast.newNumberLiteral(String.valueOf(l_cpt)));
            l_frag.setName(l_ast.newSimpleName(l_constant.getName().toString()));
            l_frag.setInitializer(l_classInstCrea);
            FieldDeclaration l_field = l_ast.newFieldDeclaration(l_frag);
            SimpleName l_fieldName = l_ast.newSimpleName(a_type.getName().toString());
            l_field.setType(l_ast.newSimpleType(l_fieldName));
            l_field.modifiers().add(l_ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
            l_field.modifiers().add(l_ast.newModifier(ModifierKeyword.STATIC_KEYWORD));
            a_type.bodyDeclarations().add(l_field);
            currentRewriter.replace(l_field.getType(), currentRewriter.createStringPlaceholder("readonly " + l_field.getType().toString(), l_field.getType().getNodeType()), description);
            l_cpt++;
        }
    }

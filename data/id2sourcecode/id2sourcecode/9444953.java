        public void addBlock(String moduleName, List<CompileParserException> errors) throws CompileParserException {
            if (isAction()) {
                owner.addWarning("[Compilando accion " + action.getName() + "]");
                action.digest(moduleName, lines, errors);
                owner.getGrammar().addAction(action);
            } else if (isObject()) {
                owner.addWarning("[Compilando objeto " + obj.getId() + "]");
                obj.digest(moduleName, lines, errors);
                owner.addObject(obj);
            } else if (isClass()) {
                owner.addWarning("[Compilando clase " + obj.getId() + "]");
                obj.digest(moduleName, lines, errors);
                owner.addClass(obj);
            } else if (isFunction()) {
                owner.addWarning("[Compilando funcion " + name + "]");
                owner.addFunction(name, lines);
            }
        }

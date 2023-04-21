    @Methode
    private boolean tryParseForField() throws Exception {
        if (!this.expressionStatement & this.getReader().lookAhead(this.tokens.ASSIGNMENT, false)) {
            if (!this.canBeWrittenTo) throw this.makeParserUtils().makeExceptionUser("Keine Zuweisung erlaubt");
            this.getReader().nextToken();
            Expression exp = this.makeExpressionParser().parseExpression(this.scope);
            if (!exp.getType().canBeAssignedTo(this.akType)) throw this.makeParserUtils().makeExceptionUser("Expression " + exp.toString() + " kann nicht zugewiesen werden. Typen nicht kompatibel");
            this.bytes = new ByteWriter();
            this.bytes.writeAll(this.writeToCodeBefore);
            this.makeParserUtils().writeExpression(this.bytes, exp);
            this.bytes.writeAll(this.writeToCodeAfter);
            this.akType = null;
            this.fertig = true;
            return false;
        } else if (this.getReader().lookAhead(this.tokens.ECKIGE_KLAMMER_AUF)) {
            if (!this.akType.isArray()) throw this.makeParserUtils().makeExceptionUser("Array-Zugriff auf nicht Array-Typ.");
            Expression index = this.makeExpressionParser().parseExpression(this.scope);
            if (!index.isInteger()) throw this.makeParserUtils().makeExceptionUser("Array-Index muss ein Integer-Wert sein.");
            this.getReader().erwarte(this.tokens.ECKIGE_KLAMMER_ZU);
            this.akType = this.akType.getBaseType();
            this.writeToCodeBefore = new ByteWriter();
            this.writeToCodeBefore.writeAll(this.readFromCode);
            this.makeParserUtils().writeExpression(this.writeToCodeBefore, index);
            this.makeParserUtils().writeExpression(this.readFromCode, index);
            this.readFromCode.write1Byte(this.makeParserUtils().getArrayLoadOperation(this.akType));
            this.writeToCodeAfter = new ByteWriter();
            this.writeToCodeAfter.write1Byte(this.makeParserUtils().getArrayStoreOperation(this.akType));
            this.canBeWrittenTo = true;
            this.fertig = this.expressionStatement;
            return true;
        } else if (this.getReader().lookAhead(this.tokens.DOT)) {
            if (this.akType.isArray()) {
                if (!this.getReader().erwarte(this.tokens.IDENTIFIER).getText().equals("length")) throw this.makeParserUtils().makeExceptionUser("Bei Arrays gibt es nur .length");
                Type type = new Type();
                this.akType = new Type(type.INT);
                this.writeToCodeBefore = null;
                this.writeToCodeAfter = null;
                this.readFromCode.write1Byte(this.Operations.ARRAYLENGTH);
                this.fertig = this.expressionStatement;
                this.canBeWrittenTo = false;
                return false;
            }
            if (!this.akType.isClass()) throw this.makeParserUtils().makeExceptionUser("Element-Zugriff auf nicht-Klassen-Typ.");
            String idName = this.getReader().erwarte(this.tokens.IDENTIFIER).getText();
            if (this.getReader().lookAhead(this.tokens.RUNDE_KLAMMER_AUF, false)) {
                ByteWriter paramBytes = new ByteWriter();
                Signature params = this.getParser().parseParameterValues(paramBytes, this.scope);
                Method method = this.akType.getClazz().getMethod(idName, params);
                this.akType = method.getReturnType();
                if (method.isStatic()) this.readFromCode.write1Byte(this.Operations.POP);
                this.readFromCode.writeAll(paramBytes);
                this.readFromCode.write1Byte(method.getInvokeOperation());
                this.readFromCode.write2Byte(method.getConstantPoolIndex());
                this.bytes = new ByteWriter();
                this.bytes.writeAll(this.readFromCode);
                this.writeToCodeBefore = null;
                this.writeToCodeAfter = null;
                this.canBeWrittenTo = false;
                this.fertig = true;
                return true;
            } else {
                Field feld = this.akType.getClazz().getField(idName);
                if (feld == null) throw new ParseException("Das Feld " + idName.toString() + " existiert nicht im Typ " + this.akType.toString());
                this.akType = feld.getType();
                this.writeToCodeBefore = new ByteWriter();
                this.writeToCodeBefore.writeAll(this.readFromCode);
                this.writeToCodeAfter = new ByteWriter();
                if (feld.isStatic()) {
                    this.writeToCodeAfter.write1Byte(this.Operations.POP);
                    this.writeToCodeAfter.write1Byte(this.Operations.PUTSTATIC);
                } else this.writeToCodeAfter.write1Byte(this.Operations.PUTFIELD);
                this.writeToCodeAfter.write2Byte(feld.getConstantPoolIndex());
                if (feld.isStatic()) {
                    this.readFromCode.write1Byte(this.Operations.POP);
                    this.readFromCode.write1Byte(this.Operations.GETSTATIC);
                } else this.readFromCode.write1Byte(this.Operations.GETFIELD);
                this.readFromCode.write2Byte(feld.getConstantPoolIndex());
                this.canBeWrittenTo = true;
                this.fertig = this.expressionStatement;
                return true;
            }
        } else if (!this.expressionStatement & this.getReader().lookAhead(this.tokens.INC, false)) {
            this.getReader().nextToken();
            if (!this.akType.isInteger()) throw this.makeParserUtils().makeExceptionUser("Typ " + this.akType.toString() + " kann nicht inkrementiert werden.");
            this.bytes = new ByteWriter();
            this.bytes.writeAll(this.writeToCodeBefore);
            this.bytes.writeAll(this.readFromCode);
            this.bytes.write1Byte(this.Operations.ICONST_1);
            this.bytes.write1Byte(this.Operations.IADD);
            this.bytes.writeAll(this.writeToCodeAfter);
            this.fertig = true;
            this.canBeWrittenTo = false;
            this.akType = null;
            return false;
        } else if (!this.expressionStatement & this.getReader().lookAhead(this.tokens.DEC, false)) {
            this.getReader().nextToken();
            if (!this.akType.isInteger()) throw this.makeParserUtils().makeExceptionUser("Typ " + this.akType.toString() + " kann nicht dekrementiert werden.");
            this.bytes = new ByteWriter();
            this.bytes.writeAll(this.writeToCodeBefore);
            this.bytes.writeAll(this.readFromCode);
            this.bytes.write1Byte(this.Operations.ICONST_1);
            this.bytes.write1Byte(this.Operations.ISUB);
            this.bytes.writeAll(this.writeToCodeAfter);
            this.fertig = true;
            this.canBeWrittenTo = false;
            this.akType = null;
            return false;
        }
        return false;
    }

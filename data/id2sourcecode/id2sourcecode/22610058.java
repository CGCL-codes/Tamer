    @Methode
    public Expression parseExpressionStatement(Scope scope) throws Exception {
        Field f;
        try {
            f = scope.getFieldByName(this.getReader().erwarte(this.tokens.IDENTIFIER).getText());
        } catch (UserParseException e) {
            throw this.makeParserUtils().makeExceptionUser("Feld nicht gefunden", e);
        }
        this.scope = scope;
        this.akType = f.getType();
        this.readFromCode = new ByteWriter();
        this.writeToCodeBefore = new ByteWriter();
        this.writeToCodeAfter = new ByteWriter();
        this.canBeWrittenTo = true;
        this.expressionStatement = true;
        this.fertig = true;
        this.readFromCode.write1Byte(this.makeParserUtils().getLoadOperation(f.getType()));
        this.readFromCode.write1Byte(scope.getFieldIndex(f));
        this.writeToCodeAfter.write1Byte(this.makeParserUtils().getStoreOperation(f.getType()));
        this.writeToCodeAfter.write1Byte(scope.getFieldIndex(f));
        while (this.tryParseForField()) ;
        if (!this.fertig) throw this.makeParserUtils().makeExceptionUser("Statement nicht fertig.");
        return new Expression(this.akType, this.readFromCode);
    }

    @Methode
    private ByteWriter parseSonst(Scope scope) throws Exception {
        Field f = scope.getFieldByName(this.getReader().erwarte(this.tokens.IDENTIFIER).getText());
        this.scope = scope;
        this.akType = f.getType();
        this.readFromCode = new ByteWriter();
        this.writeToCodeBefore = new ByteWriter();
        this.writeToCodeAfter = new ByteWriter();
        this.canBeWrittenTo = true;
        this.bytes = null;
        this.expressionStatement = false;
        this.fertig = false;
        this.readFromCode.write1Byte(this.makeParserUtils().getLoadOperation(f.getType()));
        this.readFromCode.write1Byte(scope.getFieldIndex(f));
        this.writeToCodeAfter.write1Byte(this.makeParserUtils().getStoreOperation(f.getType()));
        this.writeToCodeAfter.write1Byte(scope.getFieldIndex(f));
        while (this.tryParseForField()) ;
        if (!this.fertig) throw this.makeParserUtils().makeExceptionUser("Statement nicht fertig.");
        if (this.akType != null) if (!this.akType.isVoid()) this.bytes.write1Byte(this.Operations.POP);
        return this.bytes;
    }

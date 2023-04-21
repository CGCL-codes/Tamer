    public void setUp() throws InterpreterException {
        testHost_ = new NATObject();
        testHost_.meta_defineField(AGSymbol.jAlloc("x"), NATNumber.ONE);
        foo_ = AGSymbol.jAlloc("foo");
        ctx_.base_lexicalScope().meta_defineField(AGSymbol.jAlloc("Field"), NativeTypeTags._FIELD_);
        testField_ = evalAndReturn("object: { def name := `foo;" + "def host := nil; def init(newhost) { host := newhost; };" + "def v := nil;" + "def readField() { v };" + "def writeField(n) { v := n+1 } } taggedAs: [ Field ]");
    }

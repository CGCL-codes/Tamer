    public void testCustomFieldSlotRemoval() throws InterpreterException {
        ATSymbol x_sym = AGSymbol.jAlloc("x");
        ATObject obj = evalAndReturn("def removalObj := object: { }");
        obj.meta_addField(evalAndReturn("" + "deftype Field;" + "object: {" + "  def name := `x;" + "  def readField() { 1 };" + "  def writeField(newx) { };" + "  def accessor() { (&readField).method };" + "  def mutator() { (&writeField).method };" + "} taggedAs: [Field]").asField());
        assertTrue(obj.meta_respondsTo(x_sym).asNativeBoolean().javaValue);
        assertEquals(4, obj.meta_listSlots().base_length().asNativeNumber().javaValue);
        assertEquals(NATNumber.ONE, obj.meta_removeSlot(x_sym));
        evalAndTestException("removalObj.x", XSelectorNotFound.class);
        assertEquals(2, obj.meta_listSlots().base_length().asNativeNumber().javaValue);
    }

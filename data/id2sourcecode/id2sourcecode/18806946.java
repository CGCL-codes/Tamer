    public void testMethodInvocation10() {
        String cuText = "interface I{" + "	void write();" + "	void read();" + "}" + "abstract class X1 implements I {" + "	static int i=9;" + "	static void have(){}" + "	abstract void pick();" + "}" + "class X2 extends X1 {//error! lack of unimplement abstract method of X \n" + "	public void read() {  }" + "	public void write(){  }" + "}";
        CompilationUnit cu = null;
        try {
            cu = runIt(cuText).get(0);
        } catch (ParserException e) {
            fail(e.getMessage());
        }
        try {
            new SemanticsChecker(sc).check(cu);
        } catch (Exception e) {
        }
    }

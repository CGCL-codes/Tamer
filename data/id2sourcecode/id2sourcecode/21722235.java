    public void test_0115_while_RAC_invalid_variant() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X {\n" + "   public void m() { \n" + "   	int i = 0;\n" + "   	int[] vals = new int[] {2,3,5,7,9};\n" + "   	int sum = 0;\n" + "       //@ decreases  i;\n" + "       while (i < vals.length) {\n" + "             //@ decreases vals.length - i;\n" + "             while (i%2==0) \n" + "                   sum += vals[i++]; } \n" + "}\n" + "}\n", "new X().m()", null, JMLLoopVariantError.class);
    }

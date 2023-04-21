    public void test_3113_while_RAC_valid_variant() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X {\n" + "   public void m() { \n" + "   	int i = 0;\n" + "   	int[] vals = new int[] {2,3,5,7,9};\n" + "   	int sum = 0;\n" + "       //@ decreases vals.length - i;\n" + "       do {\n" + "             //@ decreases vals.length - i;\n" + "             do { \n" + "              sum += vals[i--];} while (i%2==0);\n" + "		} while (i < vals.length); \n" + "	}\n" + "}\n", "new X().m()", null, JMLLoopVariantError.class);
    }

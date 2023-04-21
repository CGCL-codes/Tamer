package org.rakiura.evm;

/**
 * Arithmetic negation operation. 
 * 
 * <br><br>
 * OpNeg.java
 * Created: 26/08/2004 11:55:07 
 *
 * @author <a href="mailto:mariusz@rakiura.org">Mariusz Nowostawski</a>
 * @version @version@ $Revision: 1.7 $
 * 
 */
public class OpNeg extends Instruction {

    static final OpNeg theInstance = new OpNeg();

    private OpNeg() {
    }

    public static OpNeg getInstance() {
        return theInstance;
    }

    public void execute(ExecutionEngine frame) throws SizeLimitExceededException {
        if (frame.stack.size() == 0) {
            frame.error();
            return;
        }
        int a1 = frame.stack.pop();
        frame.stack.push(-a1);
    }

    public String getMnemonic() {
        return "neg";
    }
}

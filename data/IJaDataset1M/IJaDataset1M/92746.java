package gov.nasa.jpf.symbc.bytecode;

import gov.nasa.jpf.jvm.KernelState;
import gov.nasa.jpf.jvm.SystemState;
import gov.nasa.jpf.jvm.ThreadInfo;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import gov.nasa.jpf.jvm.StackFrame;
import gov.nasa.jpf.jvm.ChoiceGenerator;
import gov.nasa.jpf.symbc.numeric.*;

/**
 * Convert long to float
 * ..., value => ..., result
 */
public class L2F extends gov.nasa.jpf.jvm.bytecode.L2F {

    public Instruction execute(SystemState ss, KernelState ks, ThreadInfo th) {
        IntegerExpression sym_lval = (IntegerExpression) th.getTopFrame().getLongOperandAttr();
        if (sym_lval == null) {
            return super.execute(ss, ks, th);
        } else {
            ChoiceGenerator<?> cg;
            if (!th.isFirstStepInsn()) {
                cg = new PCChoiceGenerator(1);
                ss.setNextChoiceGenerator(cg);
                return this;
            } else {
                cg = ss.getChoiceGenerator();
                assert (cg instanceof PCChoiceGenerator) : "expected PCChoiceGenerator, got: " + cg;
            }
            PathCondition pc;
            ChoiceGenerator<?> prev_cg = cg.getPreviousChoiceGenerator();
            while (!((prev_cg == null) || (prev_cg instanceof PCChoiceGenerator))) {
                prev_cg = prev_cg.getPreviousChoiceGenerator();
            }
            if (prev_cg == null) pc = new PathCondition(); else pc = ((PCChoiceGenerator) prev_cg).getCurrentPC();
            assert pc != null;
            th.longPop();
            th.push(0, false);
            SymbolicReal sym_fval = new SymbolicReal();
            StackFrame sf = th.getTopFrame();
            sf.setOperandAttr(sym_fval);
            pc._addDet(Comparator.EQ, sym_fval, sym_lval);
            if (!pc.simplify()) {
                ss.setIgnored(true);
            } else {
                ((PCChoiceGenerator) cg).setCurrentPC(pc);
            }
            return getNext(th);
        }
    }
}

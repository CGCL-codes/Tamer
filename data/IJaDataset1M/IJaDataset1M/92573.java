package com.rapidminer.operator.learner.functions.kernel;

import java.util.Iterator;
import java.util.List;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.OperatorCapability;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.UserError;
import com.rapidminer.operator.learner.functions.kernel.jmysvm.examples.SVMExamples;
import com.rapidminer.operator.learner.functions.kernel.jmysvm.kernel.Kernel;
import com.rapidminer.operator.learner.functions.kernel.jmysvm.svm.SVMInterface;
import com.rapidminer.operator.learner.functions.kernel.logistic.KLR;
import com.rapidminer.parameter.ParameterType;

/**
 * This is the Java implementation of <em>myKLR</em> by Stefan R&uuml;ping.
 * myKLR is a tool for large scale kernel logistic regression based on the
 * algorithm of Keerthi/etal/2003 and the code of mySVM.
 * 
 * @rapidminer.index KLR
 * @author Ingo Mierswa
 */
public class MyKLRLearner extends AbstractMySVMLearner {

    public MyKLRLearner(OperatorDescription description) {
        super(description);
    }

    @Override
    public boolean supportsCapability(OperatorCapability lc) {
        if (lc == OperatorCapability.NUMERICAL_ATTRIBUTES) return true;
        if (lc == OperatorCapability.BINOMINAL_LABEL) return true;
        return false;
    }

    @Override
    public AbstractMySVMModel createSVMModel(ExampleSet exampleSet, SVMExamples sVMExamples, Kernel kernel, int kernelType) {
        return new MyKLRModel(exampleSet, sVMExamples, kernel, kernelType);
    }

    @Override
    public SVMInterface createSVM(Attribute label, Kernel kernel, SVMExamples sVMExamples, com.rapidminer.example.ExampleSet rapidMinerExamples) throws OperatorException {
        if (!label.isNominal()) throw new UserError(this, 101, new Object[] { "MyKLR", label.getName() });
        return new KLR(this);
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        Iterator<ParameterType> p = types.iterator();
        while (p.hasNext()) {
            ParameterType type = p.next();
            if (type.getKey().equals(PARAMETER_C)) {
                type.setDefaultValue(Double.valueOf(1.0d));
            }
        }
        return types;
    }
}

package org.jdmp.core.algorithm.classification;

import org.jdmp.core.dataset.RegressionDataSet;
import org.jdmp.core.sample.Sample;
import org.jdmp.core.variable.Variable;
import org.ujmp.core.Matrix;
import org.ujmp.core.mapmatrix.DefaultMapMatrix;
import org.ujmp.core.mapmatrix.MapMatrix;

public class ConstantClassifier extends AbstractClassifier {

    private static final long serialVersionUID = 4554183836593870371L;

    private Matrix prediction = null;

    private long maxClassId = 0;

    public ConstantClassifier() {
        super();
    }

    public Matrix predict(Matrix input, Matrix sampleWeight) throws Exception {
        return prediction;
    }

    public void reset() throws Exception {
        prediction = null;
        maxClassId = 0;
    }

    public void train(RegressionDataSet dataSet) throws Exception {
        MapMatrix<Long, Integer> count = new DefaultMapMatrix<Long, Integer>();
        for (Sample s : dataSet.getSamples()) {
            Matrix m = s.getVariables().getMatrix(Variable.TARGET);
            long target = m.getCoordinatesOfMaximum()[COLUMN];
            maxClassId = Math.max(maxClassId, target);
            Integer c = count.get(target);
            if (c == null) {
                c = 0;
            }
            c++;
            count.put(target, c);
        }
        int max = 0;
        long pc = 0;
        for (Long t : count.keySet()) {
            Integer c = count.get(t);
            if (c > max) {
                max = c;
                pc = t;
            }
        }
        prediction = Matrix.factory.zeros(1, maxClassId + 1);
        prediction.setAsDouble(1.0, 0, pc);
    }

    public Classifier emptyCopy() throws Exception {
        return new ConstantClassifier();
    }
}

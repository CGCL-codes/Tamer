package la4j.decomposition;

import la4j.err.MatrixDecompositionException;
import la4j.factory.Factory;
import la4j.matrix.Matrix;
import la4j.vector.Vector;

public class LUDecompositor implements MatrixDecompositor {

    @Override
    public Matrix[] decompose(Matrix matrix, Factory factory) throws MatrixDecompositionException {
        if (matrix.rows() != matrix.columns()) throw new MatrixDecompositionException();
        Matrix lu = matrix.clone();
        for (int j = 0; j < lu.columns(); j++) {
            Vector jcolumn = lu.getColumn(j);
            for (int i = 0; i < lu.rows(); i++) {
                int kmax = Math.min(i, j);
                double s = 0.0;
                for (int k = 0; k < kmax; k++) {
                    s += lu.get(i, k) * jcolumn.get(k);
                }
                jcolumn.set(i, jcolumn.get(i) - s);
                lu.set(i, j, jcolumn.get(i));
            }
            int p = j;
            for (int i = j + 1; i < lu.rows(); i++) {
                if (Math.abs(jcolumn.get(i)) > Math.abs(jcolumn.get(p))) p = i;
            }
            if (p != j) {
                for (int k = 0; k < lu.columns(); k++) {
                    double t = lu.get(p, k);
                    lu.set(p, k, lu.get(j, k));
                    lu.set(j, k, t);
                }
            }
            if (j < lu.rows() & lu.get(j, j) != 0.0) {
                for (int i = j + 1; i < lu.rows(); i++) {
                    lu.set(i, j, lu.get(i, j) / lu.get(j, j));
                }
            }
        }
        Matrix l = factory.createMatrix(lu.rows(), lu.columns());
        for (int i = 0; i < l.rows(); i++) {
            for (int j = 0; j <= i; j++) {
                if (i > j) l.set(i, j, lu.get(i, j)); else if (i == j) l.set(i, j, 1.0);
            }
        }
        Matrix u = factory.createMatrix(lu.columns(), lu.columns());
        for (int i = 0; i < u.columns(); i++) {
            for (int j = i; j < u.columns(); j++) {
                if (i <= j) u.set(i, j, lu.get(i, j));
            }
        }
        return new Matrix[] { l, u };
    }
}

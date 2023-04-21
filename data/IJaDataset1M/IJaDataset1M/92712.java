package com.mockturtlesolutions.snifflib.pde;

import com.mockturtlesolutions.snifflib.datatypes.DblMatrix;

public interface PDECoefficient {

    public DblMatrix getValueAt(DblMatrix[] x);

    public DblMatrix getValueAt(DblMatrix[] x, DblMatrix time);
}

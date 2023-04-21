    private void split(List indexes, Timer timer) {
        m_cluster1 = new ArrayList();
        m_cluster2 = new ArrayList();
        if (indexes.size() <= 1) {
            return;
        }
        timer.start("Generate W, D");
        DoubleMatrix2D adjacencyMatrix = DoubleFactory2D.dense.make(indexes.size(), indexes.size());
        for (int i = 0; i < indexes.size(); i++) {
            int temp1 = (Integer) indexes.get(i);
            for (int j = 0; j < indexes.size(); j++) {
                int temp2 = (Integer) indexes.get(j);
                adjacencyMatrix.set(i, j, graphAdjacencyMatrix.get(temp1, temp2));
            }
        }
        if (m_log) {
            System.out.println("Ajacency matrix:" + adjacencyMatrix);
        }
        DoubleMatrix2D diagonalMatrix = DoubleFactory2D.dense.make(indexes.size(), indexes.size());
        for (int row = 0; row < indexes.size(); row++) {
            double sum = 0;
            for (int col = 0; col < indexes.size(); col++) {
                sum += adjacencyMatrix.get(row, col);
            }
            diagonalMatrix.set(row, row, sum);
        }
        if (m_log) {
            System.out.println("Diagonal matrix:" + diagonalMatrix);
        }
        timer.end();
        timer.start("Compute laplacian matrix");
        DoubleMatrix2D laplacianMatrix = diagonalMatrix.copy().assign(adjacencyMatrix, Functions.minus);
        timer.end();
        if (m_log) {
            System.out.println("Laplacian matrix:" + laplacianMatrix);
        }
        timer.start("Cholesky decomposition");
        LanczosDecomposition lanczosDecomposition = new LanczosDecomposition(laplacianMatrix, getLanczosK(laplacianMatrix), false);
        DoubleMatrix1D eigenvalues;
        DoubleMatrix2D eigenvectors;
        eigenvalues = lanczosDecomposition.getRealEigenvalues();
        eigenvectors = lanczosDecomposition.getV();
        timer.end();
        if (m_log) {
            System.out.println("Eigenvalues:" + eigenvalues);
            System.out.println("Eigenvectors:" + eigenvectors);
        }
        timer.start("Select eigenvector");
        double minEigenvalue = Double.POSITIVE_INFINITY;
        int minEigenvalueIndex = 0;
        for (int index = 0; index < eigenvalues.size(); index++) {
            if (eigenvalues.get(index) < minEigenvalue) {
                boolean hasNegAndPos = false;
                double lastVal = 0;
                for (int j = 0; (j < eigenvectors.rows()) && (!hasNegAndPos); j++) {
                    double v = eigenvectors.get(j, index);
                    if (lastVal < 0 && v > 0) hasNegAndPos = true; else if (lastVal >= 0 && v < 0) hasNegAndPos = true; else lastVal = v;
                }
                if (hasNegAndPos) {
                    minEigenvalue = eigenvalues.get(index);
                    minEigenvalueIndex = index;
                }
            }
        }
        timer.end();
        DoubleMatrix1D eigenvector = eigenvectors.viewColumn(minEigenvalueIndex);
        if (m_log) {
            System.out.println("Selected eigenvalue:" + eigenvalues.get(minEigenvalueIndex));
            System.out.println("Selected eigenvector:" + eigenvector);
        }
        timer.start("Cluster");
        double maxComponent = eigenvector.get(0), minComponent = eigenvector.get(0);
        for (int index = 1; index < eigenvector.size(); index++) {
            if (maxComponent < eigenvector.get(index)) {
                maxComponent = eigenvector.get(index);
            }
            if (minComponent > eigenvector.get(index)) {
                minComponent = eigenvector.get(index);
            }
        }
        double split = (maxComponent + minComponent) / 2;
        if (m_log) {
            System.out.println("Split value:" + split);
        }
        for (int index = 0; index < eigenvector.size(); index++) {
            if (eigenvector.get(index) <= split) {
                m_cluster1.add(index);
            } else {
                m_cluster2.add(index);
            }
        }
        timer.end();
    }

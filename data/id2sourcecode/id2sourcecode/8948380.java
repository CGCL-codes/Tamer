    public void split(List dots, Timer timer) {
        m_cluster1 = new ArrayList();
        m_cluster2 = new ArrayList();
        if (dots.size() <= 1) {
            return;
        }
        timer.start("Generate W, D");
        DoubleMatrix2D weightMatrix = DoubleFactory2D.dense.make(dots.size(), dots.size());
        for (int row = 0; row < dots.size(); row++) {
            Dot a = (Dot) dots.get(row);
            for (int col = 0; col < dots.size(); col++) {
                Dot b = (Dot) dots.get(col);
                weightMatrix.set(row, col, a.getWeight(b, m_radius, m_delta));
            }
        }
        if (m_log) {
            System.out.println("Weight matrix:" + weightMatrix);
        }
        DoubleMatrix2D distanceMatrix = DoubleFactory2D.dense.make(dots.size(), dots.size());
        for (int row = 0; row < dots.size(); row++) {
            double sum = 0;
            for (int col = 0; col < dots.size(); col++) {
                sum += weightMatrix.get(row, col);
            }
            distanceMatrix.set(row, row, sum);
        }
        timer.end();
        if (m_log) {
            System.out.println("Distance matrix:" + distanceMatrix);
        }
        timer.start("Compute difference matrix");
        DoubleMatrix2D differenceMatrix = distanceMatrix.copy().assign(weightMatrix, Functions.minus);
        timer.end();
        timer.start("Cholesky decomposition");
        CholeskyDecomposition choleskyDecomposition = new CholeskyDecomposition(distanceMatrix);
        DoubleMatrix2D rootDistanceMatrix = choleskyDecomposition.getL();
        timer.end();
        if (m_log) {
            System.out.println("Cholesky decomposition matrix:" + rootDistanceMatrix);
        }
        timer.start("Compute eigen matrix");
        DoubleMatrix2D half = Algebra.DEFAULT.transpose(Algebra.DEFAULT.solveTranspose(Algebra.DEFAULT.transpose(rootDistanceMatrix), differenceMatrix));
        DoubleMatrix2D eigenMatrix = Algebra.DEFAULT.solve(rootDistanceMatrix, half);
        timer.end();
        if (m_log) {
            System.out.println("Eigenmatrix:" + eigenMatrix);
        }
        DoubleMatrix1D eigenvalues;
        DoubleMatrix2D eigenvectors;
        if (m_algorithm.equals("Lanczos")) {
            timer.start("Eigenvalue decomposition");
            LanczosDecomposition lanczosDecomposition = new LanczosDecomposition(eigenMatrix, getLanczosK(eigenMatrix), m_log);
            timer.end();
            eigenvalues = lanczosDecomposition.getRealEigenvalues();
            eigenvectors = lanczosDecomposition.getV();
        } else if (m_algorithm.equals("QL")) {
            timer.start("Eigenvalue decomposition");
            EigenvalueDecomposition eigenvalueDecomposition = new EigenvalueDecomposition(eigenMatrix);
            timer.end();
            eigenvalues = eigenvalueDecomposition.getRealEigenvalues();
            eigenvectors = eigenvalueDecomposition.getV();
        } else {
            throw new IllegalArgumentException("Unrecognized algorithm: " + m_algorithm);
        }
        if (m_log) {
            System.out.println("Eigenvalues:" + eigenvalues);
        }
        timer.start("Select eigenvector");
        double minEigenvalue = eigenvalues.get(0);
        int minEigenvalueIndex = 0;
        for (int index = 1; index < eigenvalues.size(); index++) {
            if (eigenvalues.get(index) < minEigenvalue) {
                minEigenvalue = eigenvalues.get(index);
                minEigenvalueIndex = index;
            }
        }
        DoubleMatrix1D eigenvector = Algebra.DEFAULT.mult(Algebra.DEFAULT.inverse(Algebra.DEFAULT.transpose(rootDistanceMatrix)), eigenvectors.viewColumn(minEigenvalueIndex));
        timer.end();
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
            Dot dot = (Dot) dots.get(index);
            if (eigenvector.get(index) <= split) {
                m_cluster1.add(dot);
            } else {
                m_cluster2.add(dot);
            }
        }
        timer.end();
    }

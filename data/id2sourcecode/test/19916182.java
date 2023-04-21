    public TF3D_Matrix Copy() {
        TF3D_Matrix mat = new TF3D_Matrix();
        mat.grid[0][0] = grid[0][0];
        mat.grid[1][0] = grid[1][0];
        mat.grid[2][0] = grid[2][0];
        mat.grid[3][0] = grid[3][0];
        mat.grid[0][1] = grid[0][1];
        mat.grid[1][1] = grid[1][1];
        mat.grid[2][1] = grid[2][1];
        mat.grid[3][1] = grid[3][1];
        mat.grid[0][2] = grid[0][2];
        mat.grid[1][2] = grid[1][2];
        mat.grid[2][2] = grid[2][2];
        mat.grid[3][2] = grid[3][2];
        mat.grid[0][3] = grid[0][3];
        mat.grid[1][3] = grid[1][3];
        mat.grid[2][3] = grid[2][3];
        mat.grid[3][3] = grid[3][3];
        return mat;
    }

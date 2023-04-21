    private Position[][] contructGrid(int length, int height) {
        if (length <= 0 || height <= 0) throw new IllegalArgumentException("length and height must be > 0");
        m_positions = new Position[length][height];
        for (int i = 0; i < length; ++i) {
            for (int j = 0; j < height; ++j) {
                m_positions[i][j] = new ObstaclePosition(i, j);
            }
        }
        for (int i = 0; i < length; ++i) {
            for (int j = 0; j < height; ++j) {
                Position[] neighbours = new Position[8];
                try {
                    neighbours[0] = m_positions[i - 1][j - 1];
                } catch (Throwable t) {
                }
                try {
                    neighbours[1] = m_positions[i - 1][j];
                } catch (Throwable t) {
                }
                try {
                    neighbours[2] = m_positions[i - 1][j + 1];
                } catch (Throwable t) {
                }
                try {
                    neighbours[3] = m_positions[i][j + 1];
                } catch (Throwable t) {
                }
                try {
                    neighbours[4] = m_positions[i + 1][j + 1];
                } catch (Throwable t) {
                }
                try {
                    neighbours[5] = m_positions[i + 1][j];
                } catch (Throwable t) {
                }
                try {
                    neighbours[6] = m_positions[i + 1][j - 1];
                } catch (Throwable t) {
                }
                try {
                    neighbours[7] = m_positions[i][j - 1];
                } catch (Throwable t) {
                }
                m_positions[i][j].setNeighbours(neighbours);
            }
        }
        return m_positions;
    }

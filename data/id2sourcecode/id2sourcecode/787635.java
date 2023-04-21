    public int[][] getAreaFaceDetected() {
        int positions[][] = m_face_detector.getFaces();
        if (positions.length > 0) {
            int[][] cover_area = new int[2][2];
            int[][] points = getSortPositions(positions);
            cover_area[0][0] = points[0][0];
            cover_area[0][1] = points[0][1];
            cover_area[1][0] = points[points.length - 1][0];
            cover_area[1][1] = points[points.length - 1][1];
            System.out.println("\nFace detected in area:");
            System.out.println("Xi:" + cover_area[0][0] + " Yi:" + cover_area[0][1] + " Xf:" + cover_area[1][0] + " Yf:" + cover_area[1][1]);
            cover_area[0][0] = cover_area[0][0] - m_off_side_pixels;
            if (cover_area[0][0] < 0) cover_area[0][0] = 0;
            cover_area[0][1] = cover_area[0][1] - m_off_side_pixels;
            if (cover_area[0][1] < 0) cover_area[0][1] = 0;
            cover_area[1][0] = cover_area[1][0] + m_off_side_pixels;
            if (cover_area[1][0] > m_width) cover_area[1][0] = m_width;
            cover_area[1][1] = cover_area[1][1] + m_off_side_pixels;
            if (cover_area[1][1] > m_height) cover_area[1][1] = m_height;
            return cover_area;
        }
        return null;
    }

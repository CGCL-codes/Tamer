    private Hull divide(Vector2[] points, int first, int last) {
        int size = last - first;
        if (size == 0) {
            Vertex vertex = new Vertex();
            vertex.point = points[first];
            vertex.next = null;
            vertex.prev = null;
            Hull hull = new Hull();
            hull.root = vertex;
            hull.leftMost = vertex;
            hull.rightMost = vertex;
            hull.size = 1;
            return hull;
        } else {
            int mid = (first + last) / 2;
            Hull left = divide(points, first, mid);
            Hull right = divide(points, mid + 1, last);
            return merge(left, right);
        }
    }

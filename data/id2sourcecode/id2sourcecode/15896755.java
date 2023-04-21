    public void deleteVertex(int index) {
        Vector3D deleted = v[index];
        for (int i = index; i < v.length - 1; i++) {
            v[i] = v[i + 1];
        }
        v[v.length - 1] = deleted;
        numVertices--;
    }

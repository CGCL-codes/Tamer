    private static LinkedList delauny(Vertex avertex[]) {
        LinkedList linkedlist = new LinkedList();
        for (int i = 0; i < steps * 3; i++) {
            Vertex avertex1[] = new Vertex[3];
            avertex1[0] = avertex[i];
            avertex1[1] = avertex[i + 1];
            avertex1[2] = delauny_sub(avertex, avertex[i], avertex[i + 1]);
            if ((i <= 0 || !avertex1[2].same_position(avertex[i - 1])) && (i != 0 || !avertex1[2].same_position(avertex[steps * 3 - 1]))) linkedlist.append(avertex1);
        }
        if (linkedlist.size() == 10) return linkedlist;
        Enumeration enumeration = linkedlist.elements();
        Vertex avertex5[] = new Vertex[3];
        while (enumeration.hasMoreElements()) {
            Vertex avertex2[] = (Vertex[]) enumeration.nextElement();
            Vertex vertex = avertex2[2];
            if (get_origin(avertex, vertex) == 1) {
                avertex5[0] = avertex2[0];
                break;
            }
        }
        while (enumeration.hasMoreElements()) {
            Vertex avertex3[] = (Vertex[]) enumeration.nextElement();
            Vertex vertex1 = avertex3[2];
            if (get_origin(avertex, vertex1) == 2) {
                avertex5[1] = avertex3[0];
                break;
            }
        }
        while (enumeration.hasMoreElements()) {
            Vertex avertex4[] = (Vertex[]) enumeration.nextElement();
            Vertex vertex2 = avertex4[2];
            if (get_origin(avertex, vertex2) == 0) {
                avertex5[2] = avertex4[0];
                linkedlist.append(avertex5);
                break;
            }
        }
        return linkedlist;
    }

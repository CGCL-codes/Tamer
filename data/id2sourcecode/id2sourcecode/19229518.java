    public ArrayList toggleAllPicks(int posX, int posY) {
        int x = (posX + lastMouseX) / 2;
        int y = (posY + lastMouseY) / 2;
        int dx = Math.abs(posX - lastMouseX) / 2;
        int dy = Math.abs(posY - lastMouseY) / 2;
        PickRenderResult[] results = view.pick(canvas, x, y, dx, dy);
        if ((results != null) && (results.length > 0)) {
            Node nodes[] = results[0].getNodes();
            if (nodes != null) {
                Object o = nodes[0].getUserData();
                if (o instanceof ActiveNode) {
                    ((ActiveNode) o).highlight(true, nodes[0]);
                    lastPickSelection.add(o);
                    lastPickSelection.add(nodes[0]);
                    System.out.println(((ActiveNode) o).getName());
                }
            }
        }
        return lastPickSelection;
    }

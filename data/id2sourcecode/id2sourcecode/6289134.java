	 * 
	 * it is saying: if the node exists already, OR if the node is near either
	 * edge, then return the node that is selected using l (location)
	 * 
	 * otherwise, return the node that is closest to the x value.
	 */
    public Node closest(double x, int[] l) {
        Node to;
        if (find(x, l)) {
            to = (Node) nodes.elementAt(l[0]);
        } else {
            int ll = l[0];
            int ul = l[0] + 1;
            Node lower = (Node) (nodes.elementAt(ll));
            Node upper = (Node) (nodes.elementAt(ul));
            while (lower.hidden()) {
                lower = (Node) (nodes.elementAt(--ll));
            }
            while (upper.hidden()) {
                upper = (Node) (nodes.elementAt(++ul));
            }
            if (ul == nodes.size() - 1) {
                to = lower;
                l[0] = ll;
            } else if (ll == 0) {
                to = upper;
                l[0] = ul;
            } else {
                double center = ((upper.getXPos() + lower.getXPos()) / 2);
                if (x >= center) {
                    to = upper;
                    l[0] = ul;
                } else {
                    to = lower;
                    l[0] = ll;
                }
            }
        }
        return to;
    }

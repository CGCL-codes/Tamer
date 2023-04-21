        protected int draw(MyCluster node, Graphics g, int w, int h, int leftcard, double xsc, double ysc, int fromy) {
            if (node.left != null && node.right != null) {
                int y = (int) (h - 30 - node.level * ysc);
                int leftx = draw(node.left, g, w, h, leftcard, xsc, ysc, y);
                int rightx = draw(node.right, g, w, h, leftcard + node.left.cardinality, xsc, ysc, y);
                g.drawLine(leftx, y, rightx, y);
                int x = (leftx + rightx) / 2;
                g.drawLine(x, y, x, fromy);
                return x;
            } else {
                int x = (int) ((leftcard + 0.5) * xsc);
                g.drawLine(x, h - 30, x, fromy);
                g.fillOval(x - 4, h - 30 - 4, 8, 8);
                g.drawString(Integer.toString(node.ID), x - 5, h - 10);
                return x;
            }
        }

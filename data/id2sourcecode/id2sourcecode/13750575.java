        private Node<E> buildTree(List<E> sorted, int l, int r) {
            Node<E> n;
            assert l >= 0;
            assert l <= r;
            if (l == r) n = new Node<E>(sorted.get(l), l); else {
                int median = (l + r) / 2;
                n = new Node<E>(sorted.get(median), median);
                if (l < median) n.left = buildTree(sorted, l, median - 1);
                if (r > median) n.right = buildTree(sorted, median + 1, r);
            }
            return n;
        }

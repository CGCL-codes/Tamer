        public List<Node> neighbours() {
            List<Node> neighbours = new ArrayList<Node>();
            for (int i = 0; i < rows.length - 1; i++) {
                int[] newrows = Arrays.copyOf(rows, rows.length);
                int temp = newrows[i];
                newrows[i] = newrows[i + 1];
                newrows[i + 1] = temp;
                neighbours.add(getNode(newrows));
            }
            return neighbours;
        }

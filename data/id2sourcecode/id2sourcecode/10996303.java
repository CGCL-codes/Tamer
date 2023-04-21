    public double median() {
        ArrayList<Double> temp = new ArrayList<Double>();
        temp = sort();
        if ((dataset.size() % 2) != 0) {
            int index = dataset.size() / 2;
            return temp.get(index);
        } else {
            if (datasetSize() == 2) {
                double left = temp.get(0);
                double right = temp.get(1);
                double median = (left + right) / 2;
                return median;
            } else {
                double left = temp.get(dataset.size() / 2);
                double right = temp.get((dataset.size() / 2) + 1);
                double median = (left + right) / 2;
                return median;
            }
        }
    }

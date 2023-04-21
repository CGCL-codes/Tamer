    private int firstIndexOf(int position) {
        int lowIndex = 0;
        int highIndex = list.size() - 1;
        while (lowIndex <= highIndex) {
            int pivotIndex = (lowIndex + highIndex) / 2;
            Event pivotElement = list.get(pivotIndex);
            int compare = pivotElement.getPosition() - position;
            if (compare < 0) {
                lowIndex = pivotIndex + 1;
            } else {
                highIndex = pivotIndex - 1;
            }
        }
        return lowIndex;
    }

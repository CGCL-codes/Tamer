    public void rearrangeCars(int from_index, int to_index) {
        int[] car_array = model.getCarArray(displayCode);
        int i;
        int moving_car = car_array[from_index];
        if (from_index > to_index) {
            for (i = from_index; i > to_index; i--) {
                car_array[i] = car_array[i - 1];
            }
        } else {
            for (i = from_index; i < to_index; i++) {
                car_array[i] = car_array[i + 1];
            }
        }
        car_array[to_index] = moving_car;
    }

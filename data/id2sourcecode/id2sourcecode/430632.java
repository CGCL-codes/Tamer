    public Ball unshift() {
        Ball ball = array[0];
        if (_lenght > 0) {
            for (int i = 0; i < _lenght - 1; i++) {
                array[i] = array[i + 1];
            }
            _lenght--;
            array[_lenght] = null;
        }
        return ball;
    }

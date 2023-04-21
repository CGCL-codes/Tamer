        private void output() {
            switch(mode.value) {
                case 1:
                    outputComparePin.write(!outputComparePin.read());
                    break;
                case 2:
                    outputComparePin.write(false);
                    break;
                case 3:
                    outputComparePin.write(true);
                    break;
            }
        }

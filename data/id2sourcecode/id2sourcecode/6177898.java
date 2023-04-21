        protected void operate() {
            if (null == pin) return;
            switch(pinmode.value) {
                case 1:
                    pin.write(!pin.read());
                    break;
                case 2:
                    pin.write(false);
                    break;
                case 3:
                    pin.write(true);
                    break;
            }
        }

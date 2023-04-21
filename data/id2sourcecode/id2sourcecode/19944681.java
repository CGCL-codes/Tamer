        private void forcedOutputCompare() {
            int count = TCNTn_reg.read() & 0xff;
            int compare = OCRn_reg.read() & 0xff;
            if (count == compare) {
                switch(COMn.getValue()) {
                    case 1:
                        if (WGMn.getValue() == MODE_NORMAL || WGMn.getValue() == MODE_CTC) outputComparePin.write(!outputComparePin.read());
                        break;
                    case 2:
                        outputComparePin.write(false);
                        break;
                    case 3:
                        outputComparePin.write(true);
                        break;
                }
            }
        }

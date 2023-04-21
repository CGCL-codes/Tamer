    public void refresh() {
        if (pins[set] < 1 && pins[reset] == 1) {
            nextStatePins[numInputs] = 0;
            nextStatePins[numInputs + 1] = 1;
        } else if (pins[set] == 1 && pins[reset] < 1) {
            nextStatePins[numInputs] = 1;
            nextStatePins[numInputs + 1] = 0;
        } else if (pins[j] == 0 && pins[k] == 1 && pins[clk] == (isRisingEdge ? 1 : 0) && valueOfClockLast == (isRisingEdge ? 0 : 1)) {
            nextStatePins[numInputs] = 0;
            nextStatePins[numInputs + 1] = 1;
        } else if (pins[j] == 1 && pins[k] == 0 && pins[clk] == (isRisingEdge ? 1 : 0) && valueOfClockLast == (isRisingEdge ? 0 : 1)) {
            nextStatePins[numInputs] = 1;
            nextStatePins[numInputs + 1] = 0;
        } else if (pins[j] == 1 && pins[k] == 1 && pins[clk] == (isRisingEdge ? 1 : 0) && valueOfClockLast == (isRisingEdge ? 0 : 1)) {
            nextStatePins[numInputs] = pins[numInputs + 1];
            nextStatePins[numInputs + 1] = pins[numInputs];
        } else if (pins[j] == 0 && pins[k] == 0 && pins[clk] == (isRisingEdge ? 1 : 0) && valueOfClockLast == (isRisingEdge ? 0 : 1)) {
            nextStatePins[numInputs] = pins[numInputs];
            nextStatePins[numInputs + 1] = pins[numInputs + 1];
        }
        valueOfClockLast = pins[clk];
    }

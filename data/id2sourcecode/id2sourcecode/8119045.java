    public void refresh() {
        if (pins[s] == -1 && pins[r] == -1) {
            pins[numInputs] = 0;
            pins[numInputs + 1] = 1;
            return;
        }
        if (pins[s] == 1 && pins[r] < 1) {
            nextStatePins[numInputs] = 1;
            nextStatePins[numInputs + 1] = 0;
            return;
        }
        if (pins[s] < 1 && pins[r] == 1) {
            nextStatePins[numInputs] = 0;
            nextStatePins[numInputs + 1] = 1;
            return;
        }
        if (pins[s] == 1 && pins[r] == 1 && timesRefreshed % 3 == 0) {
            nextStatePins[numInputs] = pins[numInputs + 1];
            nextStatePins[numInputs + 1] = pins[numInputs];
        }
        timesRefreshed++;
    }

    public void buttonPressed(int code) {
        if (code >= ButtonPressedEvent.NUM0 && code <= ButtonPressedEvent.NUM9) {
            int dig = code - ButtonPressedEvent.NUM0;
            tvControls.getChannelSelector().appendDigit(dig);
        }
    }

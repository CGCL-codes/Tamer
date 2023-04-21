    public int getChannelFilterValue() {
        String s = channelDropDown.getSelectedItem().toString();
        if (!s.equals("Ch")) return Integer.parseInt(s); else return 0;
    }

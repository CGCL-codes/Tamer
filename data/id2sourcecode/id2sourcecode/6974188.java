    public String createSubtitle() {
        String subtitle = super.createSubtitle();
        if (!hyperStack) return subtitle;
        String s = "";
        int[] dim = imp.getDimensions();
        int channels = dim[2], slices = dim[3], frames = dim[4];
        if (channels > 1) {
            s += "c:" + imp.getChannel() + "/" + channels;
            if (slices > 1 || frames > 1) s += " ";
        }
        if (slices > 1) {
            s += "z:" + imp.getSlice() + "/" + slices;
            if (frames > 1) s += " ";
        }
        if (frames > 1) s += "t:" + imp.getFrame() + "/" + frames;
        if (running2) return s;
        int index = subtitle.indexOf(";");
        if (index != -1) {
            int index2 = subtitle.indexOf("(");
            if (index2 >= 0 && index2 < index && subtitle.length() > index2 + 4 && !subtitle.substring(index2 + 1, index2 + 4).equals("ch:")) {
                index = index2;
                s = s + " ";
            }
            subtitle = subtitle.substring(index, subtitle.length());
        } else subtitle = "";
        return s + subtitle;
    }

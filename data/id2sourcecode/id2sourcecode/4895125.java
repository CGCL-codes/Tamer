    public void operate(AChannelSelection ch1, AChannelSelection ch2) {
        MMArray shape = ch2.getChannel().getSamples();
        int shapeLength = 0;
        for (int i = 0; i < shape.getLength(); i++) {
            if (shape.get(i) != 0) {
                shapeLength++;
            }
        }
        this.delayShape = new MMArray(shapeLength, 0);
        this.gainShape = new MMArray(shapeLength, 0);
        int index = 0;
        for (int i = 0; i < shape.getLength(); i++) {
            if (shape.get(i) != 0) {
                delayShape.set(index, i);
                gainShape.set(index, shape.get(i));
                index++;
            }
        }
        operate(ch1);
    }

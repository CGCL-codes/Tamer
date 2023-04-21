    public SliderInputModel(Parameter parameter, Node node) throws Exception {
        super(parameter, node);
        if (node == null) throw new Exception("A slider model cannot be used in default/parameter-free mode!");
        Node child = node.getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                if (child.getNodeName().equalsIgnoreCase("sliderLimits")) {
                    parseSliderLimits(child);
                }
            }
            child = child.getNextSibling();
        }
        if ((maxSliderValue == null) && (maxValue != null)) maxSliderValue = new Integer(maxValue);
        if ((minSliderValue == null) && (minValue != null)) minSliderValue = new Integer(minValue);
        if ((minSliderValue == null) || (maxSliderValue == null)) throw new Exception("A slider input model needs to specify slider limits or value limits!");
        int value = (minSliderValue + maxSliderValue) / 2;
        sliderModel = new DefaultBoundedRangeModel(value, 0, minSliderValue, maxSliderValue);
    }

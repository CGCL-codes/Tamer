        public MaskColor(RGB rgb) {
            RGBBase.Channel maxch = rgb.getMaxChannel();
            RGBBase.Channel medch = rgb.getMedChannel();
            RGBBase.Channel minch = rgb.getMinChannel();
            grayValue = rgb.getValue(RGBBase.Channel.W);
            primaryColorValue = rgb.getValue(maxch) - rgb.getValue(medch);
            secondaryColorValue = rgb.getValue(medch) - rgb.getValue(minch);
            primaryChannel = maxch;
            secondaryChannel = RGBBase.Channel.getChannelByArrayIndex(maxch.index + medch.index);
        }

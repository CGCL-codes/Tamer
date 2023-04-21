    protected RGB XYZToChannelByMultiMatrix(final RGB luminanceRoughRGB, final CIEXYZ channelXYZ, final RGBBase.Channel channel) {
        RGB rgb = (RGB) luminanceRoughRGB.clone();
        rgb.getValues(luminanceRGBValues);
        mmModel.correct.gammaUncorrect(rgb);
        double[] inverseMatrix = getChannelInverse(channel, rgb.getValue(channel));
        if (inverseMatrix != null) {
            rgb = XYZToChannelByMatrix(channelXYZ, inverseMatrix, channel);
            rgb.setValue(channel, rgb.getValue(channel, RGB.MaxValue.Double1) * luminanceRGBValues[channel.getArrayIndex()]);
            return rgb;
        } else {
            return null;
        }
    }

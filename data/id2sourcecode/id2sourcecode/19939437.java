    protected void closeBlock() throws IOException {
        if (channel.getLength() == 0) {
        } else if (blockValues <= Constants.MAX_NUMBER_OF_VALUES) {
            for (QNameContext contextOrder : encoderContext.getChannelOrders()) {
                List<ValueAndDatatype> lvd = encoderContext.getValueAndDatatypes(contextOrder);
                for (ValueAndDatatype vd : lvd) {
                    typeEncoder.isValid(vd.datatype, vd.value);
                    typeEncoder.writeValue(encoderContext, contextOrder, channel);
                }
            }
            finalizeStream();
        } else {
            finalizeStream();
            EncoderChannel leq100 = new ByteEncoderChannel(getStream());
            boolean wasThereLeq100 = false;
            for (QNameContext contextOrder : encoderContext.getChannelOrders()) {
                List<ValueAndDatatype> lvd = encoderContext.getValueAndDatatypes(contextOrder);
                if (lvd.size() <= Constants.MAX_NUMBER_OF_VALUES) {
                    for (ValueAndDatatype vd : lvd) {
                        typeEncoder.isValid(vd.datatype, vd.value);
                        typeEncoder.writeValue(encoderContext, contextOrder, leq100);
                    }
                    wasThereLeq100 = true;
                }
            }
            if (wasThereLeq100) {
                finalizeStream();
            }
            for (QNameContext contextOrder : encoderContext.getChannelOrders()) {
                List<ValueAndDatatype> lvd = encoderContext.getValueAndDatatypes(contextOrder);
                if (lvd.size() > Constants.MAX_NUMBER_OF_VALUES) {
                    EncoderChannel gre100 = new ByteEncoderChannel(getStream());
                    for (ValueAndDatatype vd : lvd) {
                        typeEncoder.isValid(vd.datatype, vd.value);
                        typeEncoder.writeValue(encoderContext, contextOrder, gre100);
                    }
                    finalizeStream();
                }
            }
        }
    }

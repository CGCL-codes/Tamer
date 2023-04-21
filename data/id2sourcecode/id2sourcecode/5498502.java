    public void exportDataMarray(NetcdfFileWriteable datExp) throws java.io.IOException {
        DataGroup group = null;
        DataChannel channel = null;
        Dimension xdim = datExp.addDimension("X", -1);
        for (int i = 0; i < getGroupsSize(); i++) {
            group = getGroup(i);
            DataChannel xchannel = getChannel(i, group.getXChannel());
            String var = "chn_" + i + "_x";
            datExp.addVariable(var, double.class, new Dimension[] { xdim });
            datExp.addVariableAttribute(var, "long_name", xchannel.getName());
            datExp.addVariableAttribute(var, "units", xchannel.getUnits());
            for (int j = 0; j < getChannelsSize(i); j++) {
                channel = getChannel(i, j);
                var = "chn_" + i + "_" + j + "_data";
                if (channel.getAttribute().isNormal() && (channel != xchannel)) {
                    datExp.addVariable(var, double.class, new Dimension[] { xdim });
                    datExp.addVariableAttribute(var, "long_name", channel.getName());
                    datExp.addVariableAttribute(var, "units", channel.getUnits());
                }
            }
        }
        datExp.create();
        for (int i = 0; i < getGroupsSize(); i++) {
            group = getGroup(i);
            DataChannel xchannel = getChannel(i, group.getXChannel());
            String var = "chn_" + i + "_x";
            int size = xchannel.size();
            double[] data = new double[size];
            for (int k = 0; k < size; k++) {
                data[k] = xchannel.getData(k);
            }
            datExp.write(var, ArrayAbstract.factory(data));
            for (int j = 0; j < getChannelsSize(i); j++) {
                channel = getChannel(i, j);
                var = "chn_" + i + "_" + j + "_data";
                if (channel.getAttribute().isNormal() && (channel != xchannel)) {
                    for (int k = 0; k < size; k++) {
                        data[k] = channel.getData(k);
                    }
                    datExp.write(var, ArrayAbstract.factory(data));
                }
            }
        }
    }

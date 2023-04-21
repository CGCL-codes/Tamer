        protected Object getModelValue(Object model) {
            PhotoInfo obj = (PhotoInfo) model;
            ChannelMapOperation cm = obj.getColorChannelMapping();
            ColorCurve ret = null;
            if (cm != null) {
                ret = cm.getChannelCurve(name);
            }
            return ret;
        }

    public double classifyInstance(Instance instance) {
        double classValueMin = -1;
        double classValueMax = -1;
        double classValue;
        if (m_etype == ET_MIN || m_etype == ET_BOTH) {
            classValueMin = classifyInstanceMin(instance);
        }
        if (m_etype == ET_MAX || m_etype == ET_BOTH) {
            classValueMax = classifyInstanceMax(instance);
        }
        switch(m_etype) {
            case ET_MIN:
                classValue = classValueMin;
                break;
            case ET_MAX:
                classValue = classValueMax;
                break;
            case ET_BOTH:
                classValue = (classValueMin + classValueMax) / 2;
                break;
            default:
                throw new IllegalStateException("Illegal mode type!");
        }
        return (m_ctype == CT_ROUNDED ? Utils.round(classValue) : classValue);
    }

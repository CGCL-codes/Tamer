    public void transferFrom(Map<? extends String, ?> source) {
        B bean = this.bean;
        BeanMapSpec<B> spec = this.spec;
        Object[] paramArray = paramArray();
        try {
            if ((source instanceof BeanMap) && ((BeanMap) source).spec.isSubClassOf(spec)) {
                B bbean = ((BeanMap<B>) source).bean;
                for (BeanMapSpec.Handler<B> h : spec.map.values()) {
                    if (!h.isReadOnly()) {
                        paramArray[0] = h.get(bbean);
                        h.setUseParameterArray(bean, paramArray);
                    }
                }
            } else if (source.size() <= spec.map.size() - (spec.map.size() >> 2)) {
                for (Map.Entry<? extends String, ?> e : source.entrySet()) {
                    BeanMapSpec.Handler<B> h = spec.map.get(e.getKey());
                    if ((h != null) && !h.isReadOnly()) {
                        paramArray[0] = e.getValue();
                        h.setUseParameterArray(bean, paramArray);
                    }
                }
            } else {
                for (BeanMapSpec.Handler<B> h : spec.map.values()) {
                    if (!h.isReadOnly()) {
                        Object v = source.get(h.name);
                        if ((v != null) || source.containsKey(v)) {
                            paramArray[0] = v;
                            h.setUseParameterArray(bean, paramArray);
                        }
                    }
                }
            }
        } finally {
            paramArray[0] = null;
        }
    }

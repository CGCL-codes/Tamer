    @SuppressWarnings("unchecked")
    private void handleObservesElementList() {
        final List<Observes> list = (List<Observes>) attributeMap.get(OBSERVES_ELEMENT_LIST);
        if (list != null) {
            for (Observes element : list) {
                final Element broadcaster = getElementMap().get(element.getElement());
                final String attribute = element.getAttribute();
                if (broadcaster != null && attribute != null) {
                    final String strategy = element.getStrategy();
                    UpdateStrategy updateStrategy = UpdateStrategy.READ;
                    if ("readonce".equalsIgnoreCase(strategy)) {
                        updateStrategy = UpdateStrategy.READ_ONCE;
                    } else if ("readwrite".equalsIgnoreCase(strategy)) {
                        updateStrategy = UpdateStrategy.READ_WRITE;
                    }
                    getBindingGroup().addBinding(Bindings.createAutoBinding(updateStrategy, broadcaster.getAttributeMap(), BeanProperty.<ObservableMap<String, Serializable>, Serializable>create(attribute), getAttributeMap(), BeanProperty.<ObservableMap<String, Serializable>, Serializable>create(element.getTarget() == null ? attribute : element.getTarget())));
                }
            }
        }
    }

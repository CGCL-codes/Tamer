    public void moveAfter(Topic instance, RoleField ofield, RoleField.ValueIF rfv1, RoleField.ValueIF rfv2) {
        Topic p1 = rfv1.getPlayer(ofield, instance);
        Topic p2 = rfv2.getPlayer(ofield, instance);
        TopicIF typeIf = OntopolyModelUtils.getTopicIF(instance.getTopicMap(), PSI.ON, "field-value-order");
        LocatorIF datatype = DataTypes.TYPE_STRING;
        TopicIF fieldDefinitionIf = getTopicIF();
        TopicIF topicIf = instance.getTopicIF();
        TopicIF p1topic = p1.getTopicIF();
        TopicIF p2topic = p2.getTopicIF();
        Collection<Topic> alltopics = getValues(instance, ofield);
        Map<Topic, OccurrenceIF> topics_occs = getValuesWithOrdering(instance);
        List<OccurrenceIF> occs = new ArrayList<OccurrenceIF>(topics_occs.values());
        Collections.sort(occs, new Comparator<OccurrenceIF>() {

            public int compare(OccurrenceIF occ1, OccurrenceIF occ2) {
                return ObjectUtils.compare(occ1.getValue(), occ2.getValue());
            }
        });
        TopicMapBuilderIF builder = topicIf.getTopicMap().getBuilder();
        OccurrenceIF maxOcc = (OccurrenceIF) (occs.isEmpty() ? null : occs.get(occs.size() - 1));
        int fieldOrderMax = (maxOcc == null ? 0 : Ordering.stringToOrder(maxOcc.getValue()));
        OccurrenceIF p1occ = null;
        OccurrenceIF p2occ = (OccurrenceIF) topics_occs.get(p2);
        OccurrenceIF next_occ = null;
        int fieldOrderP2;
        int nextOrder = Ordering.MAX_ORDER;
        if (p2occ == null) {
            fieldOrderP2 = (fieldOrderMax == 0 ? 0 : fieldOrderMax + Ordering.ORDER_INCREMENTS);
            p2occ = builder.makeOccurrence(topicIf, typeIf, Ordering.orderToString(fieldOrderP2), datatype);
            p2occ.addTheme(fieldDefinitionIf);
            p2occ.addTheme(p2topic);
        } else {
            fieldOrderP2 = Ordering.stringToOrder(p2occ.getValue());
            int indexP2occ = occs.indexOf(p2occ);
            if (indexP2occ < (occs.size() - 1)) next_occ = (OccurrenceIF) occs.get(indexP2occ + 1);
            if (next_occ != null) {
                int fieldOrderNext = Ordering.stringToOrder(next_occ.getValue());
                nextOrder = (fieldOrderP2 + fieldOrderNext) / 2;
                if (nextOrder != fieldOrderP2) {
                    p1occ = (OccurrenceIF) topics_occs.get(p1);
                    if (p1occ != null) {
                        p1occ.setValue(Ordering.orderToString(nextOrder));
                    } else {
                        p1occ = builder.makeOccurrence(topicIf, typeIf, Ordering.orderToString(nextOrder), datatype);
                        p1occ.addTheme(fieldDefinitionIf);
                        p1occ.addTheme(p1topic);
                    }
                }
            }
        }
        if (nextOrder == Ordering.MAX_ORDER) nextOrder = fieldOrderP2;
        if (p1occ == null) {
            nextOrder += Ordering.ORDER_INCREMENTS;
            p1occ = (OccurrenceIF) topics_occs.get(p1);
            if (p1occ != null) {
                p1occ.setValue(Ordering.orderToString(nextOrder));
            } else {
                p1occ = builder.makeOccurrence(topicIf, typeIf, Ordering.orderToString(nextOrder), datatype);
                p1occ.addTheme(fieldDefinitionIf);
                p1occ.addTheme(p1topic);
            }
            int indexP2occ = occs.indexOf(p2occ);
            if (indexP2occ > 0) {
                for (int i = indexP2occ + 1; i < occs.size(); i++) {
                    OccurrenceIF occ = (OccurrenceIF) occs.get(i);
                    nextOrder += Ordering.ORDER_INCREMENTS;
                    occ.setValue(Ordering.orderToString(nextOrder));
                }
            }
        }
        alltopics.remove(p1);
        alltopics.remove(p2);
        Iterator<Topic> aiter = alltopics.iterator();
        while (aiter.hasNext()) {
            Topic atopic = aiter.next();
            if (!topics_occs.containsKey(atopic)) {
                nextOrder += Ordering.ORDER_INCREMENTS;
                OccurrenceIF occ = builder.makeOccurrence(topicIf, typeIf, Ordering.orderToString(nextOrder), datatype);
                occ.addTheme(fieldDefinitionIf);
                occ.addTheme(atopic.getTopicIF());
            }
        }
    }

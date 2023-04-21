    private QualityBox[] loadQualities(JxObject object) {
        OntologyNode quality = this._ontology.getNodeByInternalName("snap:quality");
        HashSet<QualityBox> quality_list = new HashSet<QualityBox>();
        if (quality != null) this.recurseQualities(quality, quality_list);
        QualityBox[] quality_array = quality_list.toArray(new QualityBox[quality_list.size()]);
        Arrays.sort(quality_array);
        if (object != null) {
            JxObjectQuality[] object_qualities = object.getQualities();
            if (object_qualities != null) {
                for (JxObjectQuality oq : object_qualities) {
                    int i0 = 0;
                    int i1 = quality_array.length;
                    int n = quality_array.length;
                    do {
                        int i = (i0 + i1) / 2;
                        QualityBox qb = quality_array[i];
                        int diff = oq.getQuality().compareTo(qb.getOntologyNode());
                        if (diff == 0) {
                            qb.setSelected(true);
                            qb.setText(oq.getValue());
                            break;
                        } else if (diff < 0) i1 = i; else i0 = i;
                    } while ((n >>= 1) > 0);
                }
            }
        }
        return quality_array;
    }

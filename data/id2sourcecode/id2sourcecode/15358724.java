    public void inc(int score_diff, int ext_depth) {
        count_global_total++;
        count_total++;
        count_all++;
        if (Math.abs(score_diff) >= accepted_score_diff) {
            count_good++;
        }
        if (ext_depth > ISearch.PLY) {
            ext_depth = ISearch.PLY;
        }
        double rate = getRate();
        if (updateCurrentLevel) {
            stats[cur_ext_value].addValue(rate, rate);
        } else {
            stats[ext_depth].addValue(rate, rate);
        }
        double cur_entropy = 0;
        if (extensionMode == IExtensionMode.STATIC) {
            double static_entropy = getStaticRating(cur_ext_value);
            cur_entropy = static_entropy;
        } else if (extensionMode == IExtensionMode.DYNAMIC) {
            double dynamic_entropy = stats[cur_ext_value].getEntropy();
            cur_entropy = dynamic_entropy;
        } else if (extensionMode == IExtensionMode.MIXED) {
            double dynamic_entropy = stats[cur_ext_value].getEntropy();
            double static_entropy = getStaticRating(cur_ext_value);
            cur_entropy = (dynamic_entropy + static_entropy) / 2;
        } else {
            cur_entropy = 0;
        }
        if (rate >= cur_entropy) {
            if (cur_ext_value < stats.length - 1) {
                if (stats[cur_ext_value].getCount() > 55) {
                    cur_ext_value++;
                }
            }
        } else if (rate < cur_entropy) {
            if (cur_ext_value > 1) {
                cur_ext_value--;
            }
        }
        if (norm_count <= 0) {
            norm_count = updateInterval * getObservationFrequency();
            normalize();
        }
        norm_count--;
    }

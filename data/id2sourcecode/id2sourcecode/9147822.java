    private void sort(ChannelInfo[] info, int begin, int end, int deep) {
        if (deep < 50) {
            if (begin < end) {
                ChannelInfo tmp;
                int f = (begin + end) / 2;
                tmp = info[f];
                info[f] = info[begin];
                info[begin] = tmp;
                int p_pos = begin;
                ChannelInfo pivot = info[p_pos];
                for (int i = begin; i <= end; i++) {
                    if (info[i].userCount > pivot.userCount) {
                        p_pos++;
                        tmp = info[p_pos];
                        info[p_pos] = info[i];
                        info[i] = tmp;
                    }
                }
                tmp = info[p_pos];
                info[p_pos] = info[begin];
                info[begin] = tmp;
                sort(info, begin, p_pos - 1, deep + 1);
                sort(info, p_pos + 1, end, deep + 1);
            }
        } else {
            for (int i = begin; i <= end; i++) {
                ChannelInfo little = info[i];
                int littleindex = i;
                int littleuser = little.userCount;
                for (int j = i + 1; j <= end; j++) {
                    if (info[j].userCount > littleuser) {
                        little = info[j];
                        littleindex = j;
                        littleuser = little.userCount;
                    }
                }
                ChannelInfo tmp = info[i];
                info[i] = info[littleindex];
                info[littleindex] = tmp;
            }
        }
    }

    public boolean addElements(Vector fes) {
        try {
            synchronized (this) {
                boolean worst = false;
                int count = 0;
                for (int i = 0; i < fes.size(); i++) {
                    FrontierElement fe = (FrontierElement) fes.get(i);
                    if (blackListed(fe.url)) {
                        continue;
                    }
                    if (h.containsKey(fe.url)) {
                        continue;
                    }
                    if (v.size() == 0) {
                        v.add(fe);
                        h.put(fe.url, "");
                        resize();
                        continue;
                    }
                    if (fe.score <= getWorstScore() || worst) {
                        worst = true;
                        if ((max_size > 0) && (v.size() >= max_size)) {
                            break;
                        }
                        v.add(fe);
                        h.put(fe.url, "");
                        resize();
                        continue;
                    }
                    if (i == 0 && fe.score > getBestScore()) {
                        v.insertElementAt(fe, 0);
                        h.put(fe.url, "");
                        resize();
                        continue;
                    }
                    int start = 0;
                    int end = v.size();
                    int mid = start;
                    while ((end - start) > 1) {
                        mid = (end + start) / 2;
                        if (getScoreAt(mid) >= fe.score) {
                            start = mid;
                        } else {
                            end = mid;
                        }
                    }
                    v.insertElementAt(fe, end);
                    count++;
                    h.put(fe.url, "");
                    resize();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public int moveTimepoint(int which, double t) {
        Keyframe tempv;
        Smoothness temps;
        int newpos;
        for (newpos = 0; newpos < time.length && time[newpos] < t; newpos++) ;
        tempv = value[which];
        temps = smoothness[which];
        if (newpos > which) {
            newpos--;
            for (int i = which; i < newpos; i++) {
                value[i] = value[i + 1];
                time[i] = time[i + 1];
                smoothness[i] = smoothness[i + 1];
            }
        } else for (int i = which; i > newpos; i--) {
            value[i] = value[i - 1];
            time[i] = time[i - 1];
            smoothness[i] = smoothness[i - 1];
        }
        value[newpos] = tempv;
        time[newpos] = t;
        smoothness[newpos] = temps;
        return newpos;
    }

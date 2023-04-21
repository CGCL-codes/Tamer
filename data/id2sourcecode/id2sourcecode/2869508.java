    public Model mutate(Model m) {
        Joint joint = m.getJoint((int) Math.floor(Math.random() * m.getNumberOfJoints()));
        if (joint.getLimbCount() > 0) {
            Limb limb = joint.getLimb((int) Math.floor(Math.random() * joint.getLimbCount()));
            Joint j1 = limb.getJoint(0);
            Joint j2 = limb.getJoint(1);
            double minx = Math.min(j1.getX(), j2.getX());
            double maxx = Math.max(j1.getX(), j2.getX());
            double x = minx + (maxx - minx) / 2;
            double miny = Math.min(j1.getY(), j2.getY());
            double maxy = Math.max(j1.getY(), j2.getY());
            double y = miny + (maxy - miny) / 2;
            Joint j3 = m.spawnJoint(x, y, false);
            limb.detachJoint(j2);
            limb.attachJoint(j3);
            m.spawnSpring(j2, j3, ((Spring) limb).getK());
        }
        return m;
    }

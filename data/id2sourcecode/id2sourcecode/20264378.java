    public void writeObject(DataOutput out) throws IOException {
        super.writeObject(out);
        Vector3f dir = new Vector3f();
        ((SpotLight) node).getDirection(dir);
        control.writeVector3f(out, dir);
        out.writeFloat(((SpotLight) node).getSpreadAngle());
        out.writeFloat(((SpotLight) node).getConcentration());
    }

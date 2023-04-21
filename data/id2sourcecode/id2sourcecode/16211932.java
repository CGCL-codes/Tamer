    public String saveString() {
        StringBuffer s = new StringBuffer(Save.st(ssn) + "<c>" + this.getChannel() + "</c>" + "<t>" + this.getCtrlType() + "</t>");
        s.append(super.saveString());
        s.append(Save.et(ssn));
        return s.toString();
    }

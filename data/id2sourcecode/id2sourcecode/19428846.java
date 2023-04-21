    public String[] getServers() {
        Vector<String> members = new Vector<String>();
        for (Object m : md.getChannel().getView().getMembers()) {
            Address address = (Address) m;
            members.add(address.toString());
        }
        return members.toArray(new String[0]);
    }

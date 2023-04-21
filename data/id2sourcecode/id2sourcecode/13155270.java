    public boolean setParameters(int par) {
        if (parametersSetChannel == null) parametersSetChannel = ChannelFactory.defaultFactory().getChannel(getRefName() + ":DbgCmdSetParameters");
        parametersSetChannel.connect();
        int newpar = 0;
        try {
            if (parametersSetChannel.writeAccess()) parametersSetChannel.putVal(par); else System.out.println("setParameters() " + getRefName() + " has no permission to write.");
            newpar = parametersSetChannel.getValInt();
        } catch (ConnectionException e) {
            newpar = 100;
        } catch (GetException e) {
            newpar = 200;
        } catch (PutException e) {
            System.out.println(getRefName() + " got PutException " + e.getMessage());
        }
        return false;
    }

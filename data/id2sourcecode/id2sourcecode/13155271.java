    public boolean setHV(double hv) {
        if (hv > 0) hv = -hv;
        if (hvSetChannel == null) hvSetChannel = ChannelFactory.defaultFactory().getChannel(getRefName() + ":DbgHVBias");
        hvSetChannel.connect();
        double newhv = 0;
        try {
            if (hvSetChannel.writeAccess()) hvSetChannel.putVal(hv); else System.out.println("setHV() " + getRefName() + " has no permission to write.");
            newhv = hvSetChannel.getValDbl();
        } catch (ConnectionException e) {
            newhv = 100;
        } catch (GetException e) {
            newhv = 200;
        } catch (PutException e) {
            System.out.println(getRefName() + " got PutException " + e.getMessage());
        }
        return false;
    }

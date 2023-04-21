    public boolean setMPS(double mps) {
        if (mpsSetChannel == null) mpsSetChannel = ChannelFactory.defaultFactory().getChannel(getRefName() + ":DbgMPSPulseLossLimit");
        mpsSetChannel.connect();
        double newmps = 0;
        try {
            if (mpsSetChannel.writeAccess()) mpsSetChannel.putVal(mps); else System.out.println("setMPS() " + getRefName() + " has no permission to write.");
            newmps = mpsSetChannel.getValDbl();
        } catch (ConnectionException e) {
            newmps = -100;
        } catch (GetException e) {
            newmps = -200;
        } catch (PutException e) {
            System.out.println(getRefName() + " got PutException " + e.getMessage());
        }
        return false;
    }

    public boolean alterHV(double deltahv, double sign) {
        if (deltahv < 0) deltahv = -deltahv;
        if (hvSetChannel == null) hvSetChannel = ChannelFactory.defaultFactory().getChannel(getRefName() + ":DbgHVBias");
        hvSetChannel.connect();
        double newhv = 0, oldhv = 0, resulthv = 0;
        try {
            oldhv = hvSetChannel.getValDbl();
            newhv = oldhv - sign * deltahv;
            if (newhv > 0) newhv = 0;
            if (hvSetChannel.writeAccess()) hvSetChannel.putVal(newhv); else System.out.println("alterHV() " + getRefName() + " has no permission to write.");
            resulthv = hvSetChannel.getValDbl();
        } catch (ConnectionException e) {
            newhv = 100;
        } catch (GetException e) {
            newhv = 200;
        } catch (PutException e) {
            System.out.println(getRefName() + " got PutException " + e.getMessage());
        }
        System.out.println("alterHV() " + getRefName() + " resulthv " + resulthv + " newhv " + newhv + " oldhv " + oldhv);
        return false;
    }

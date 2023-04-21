    @SuppressWarnings("unchecked")
    public static List<ActualAuctionBiddingInfo> recvLots(final BasicConnection basicConnection, final int fromVal, final int toVal) {
        final BasicWriter w = basicConnection.getBasicWriter();
        final BasicReader r = basicConnection.getBasicReader();
        final ReadLotsComBean readLotsComBean = new ReadLotsComBean();
        readLotsComBean.setFromVal(fromVal);
        readLotsComBean.setToVal(toVal);
        w.writeBean(readLotsComBean);
        w.flush();
        final Object recvBean = r.readFixBean();
        handlePossibleErrors(recvBean);
        return (List<ActualAuctionBiddingInfo>) recvBean;
    }

    void gsizePerceived(int w, int h, String opponent) throws RevisionFailedException {
        model = new LocalWorldModel(w, h, WorldModel.agsByTeam, getTS().getAg().getBB());
        model.setOpponent(opponent);
        getTS().getAg().addBel(Literal.parseLiteral("gsize(" + w + "," + h + ")"));
        playing = true;
        cModel = CowModelFactory.getModel(teamId + myId);
        cModel.setSize(w, h);
        clModel = ClusterModelFactory.getModel(teamId + myId);
        clModel.setStepcl(-1);
        if (view != null) view.dispose();
        if (gui) {
            try {
                view = new WorldView("Herding (view of cowboy " + (getMyId() + 1) + ") -- against " + opponent, model);
            } catch (Exception e) {
                logger.info("error starting GUI");
                e.printStackTrace();
            }
        }
        if (writeStatusThread != null) writeStatusThread.reset();
    }

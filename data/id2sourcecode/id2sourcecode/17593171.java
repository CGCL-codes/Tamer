    public SitePlannerGA(MapPoint uLeft, MapPoint lRight, int aBudget, String plString, String model, String terrain, String city, CellPlanDto cellPlanDto, NetworkInfo nInfo) throws GAException {
        super(aBudget * 5, 10, 0.7, 6, 1, 0, 0, 0.1, Crossover.ctOnePoint, 10, true, true);
        upperLeft = uLeft;
        lowerRight = lRight;
        antennaBudget = aBudget;
        pathLossString = plString;
        netInfo = nInfo;
        pathLoss = new pathlossmodule.Controller(model, terrain, city);
        c2i = new CellArea();
        CellPlanDto _cellPlan = cellPlanDto;
        ArrayList<CellPlan> cellPlanArrayList = _cellPlan.getcellPlan();
        ArrayList<Channels> channelArrayList = _cellPlan.getchannels();
        cellPlan = new CellPlanElement[3][3];
        try {
            for (CellPlan plan : cellPlanArrayList) {
                if (cellPlan[(plan.getCellId() - 1)][(plan.getSectorId() - 1)] == null) {
                    CellPlanElement cellPlanElement = new CellPlanElement();
                    cellPlanElement.cellID = (plan.getCellId() - 1);
                    cellPlanElement.sectorID = plan.getSectorId() - 1;
                    for (Channels channel : channelArrayList) {
                        if (channel.getchannelId() == plan.getChannelId()) {
                            cellPlanElement.frequency = channel.getdownlinkFreq();
                            break;
                        }
                    }
                    cellPlan[(plan.getCellId() - 1)][(plan.getSectorId() - 1)] = cellPlanElement;
                }
            }
        } catch (Exception e) {
        }
        initPopulation();
    }

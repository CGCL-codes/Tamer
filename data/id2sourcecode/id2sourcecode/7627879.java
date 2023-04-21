    private String toXml(Science science) {
        StringBuilder sb = new StringBuilder("<science ");
        sb.append("lastUpdate='").append(science.getLastUpdate().getTimeInMillis()).append("' ");
        sb.append("alchemy='").append(science.getAlchemy()).append("' ");
        sb.append("tools='").append(science.getTools()).append("' ");
        sb.append("housing='").append(science.getHousing()).append("' ");
        sb.append("food='").append(science.getFood()).append("' ");
        sb.append("military='").append(science.getMilitary()).append("' ");
        sb.append("crime='").append(science.getCrime()).append("' ");
        sb.append("channeling='").append(science.getChanneling()).append("' ");
        sb.append(" />\n");
        return sb.toString();
    }

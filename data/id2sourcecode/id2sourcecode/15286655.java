    private void axisLogic() {
        boolean change1 = (yAxisMaxVal - speed) > yAxisMaxOffVal;
        boolean change2 = (speed - yAxisMinVal) < yAxisMaxOffVal;
        boolean change3 = (speed > yAxisMaxVal);
        if (change1 || change2 || change3) {
            yAxisMaxVal = speed + yAxisMaxOffVal;
            int tmpMin = yAxisMaxVal - (yAxisDelta);
            yAxisMinVal = (tmpMin) < 0 ? 0 : tmpMin;
            yAxisMidVal = (yAxisMaxVal + yAxisMinVal) / 2;
        }
    }

    public void writeSummaryDSCHTML(String configName) {
        try {
            String title = (outputPath + configName + "_summary.html");
            FileWriter fstream = new FileWriter(title);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("<html>");
            out.write("<body>");
            out.write("<h1>Summary of run:  </h1>");
            out.write("<p>");
            out.write("Decentralized Smart Charger </br> </br>");
            out.write("Number of PHEVs: " + getAllAgentsWithPHEV().size() + "</br>");
            out.write("Number of EVs: " + getAllAgentsWithEV().size() + " of which " + chargingFailureEV.size() + " could not complete their trip" + "</br>");
            out.write("</br>");
            out.write("Time </br> </br>");
            out.write("Standard charging slot length [s]:" + minChargingLength + "</br>");
            out.write("Time [ms] reading agent schedules:" + (agentReadTime - startTime) + "</br>");
            out.write("Time [ms] LP:" + (LPTime - agentReadTime) + "</br>");
            out.write("Time [ms] slot distribution:" + (distributeTime - LPTime) + "</br>");
            out.write("Time [ms] wrapping up:" + (wrapUpTime - distributeTime) + "</br>");
            for (int i = 0; i < deletedAgents.size(); i++) {
                out.write("</br>");
                out.write("DELETED AGENT: ");
                out.write("id: " + deletedAgents.get(i).toString());
                out.write("</br>");
            }
            out.write("</br>");
            out.write("CHARGING COSTS </br>");
            out.write("DSC Average charging cost of agents: " + getAverageChargingCostAgents() + "</br>");
            out.write("DSC Average charging cost of EV agents: " + getAverageChargingCostEV() + "</br>");
            out.write("DSC Average charging cost of PHEV agents: " + getAverageChargingCostPHEV() + "</br>");
            out.write("</br>");
            out.write("</br>");
            out.write("CHARGING TIME </br>");
            out.write("Average charging time of agents: " + getAverageChargingTimeAgents() + "</br>");
            out.write("Average charging time of EV agents: " + getAverageChargingTimeEV() + "</br>");
            out.write("Average charging time of PHEV agents: " + getAverageChargingTimePHEV() + "</br>");
            out.write("</br>");
            out.write("TOTAL EMISSIONS: " + getTotalEmissions() + "</br>");
            out.write("</br>");
            for (Integer hub : myHubLoadReader.deterministicHubLoadDistribution.keySet()) {
                out.write("HUB" + hub.toString() + "</br> </br>");
                out.write("Prices </br>");
                String picPrices = outputPath + "Hub/pricesHub_" + hub.toString() + ".png";
                out.write("<img src='" + picPrices + "' alt='' width='80%'");
                out.write("</br> </br>");
                out.write("Load Before and after </br>");
                String picBeforeAfter = outputPath + "Hub/deterministicLoadBeforeAfter_hub1.png";
                out.write("<img src='" + picBeforeAfter + "' alt='' width='80%'");
                out.write("</br> </br>");
            }
            out.write("</p>");
            out.write("</body>");
            out.write("</html>");
            out.close();
        } catch (Exception e) {
        }
    }

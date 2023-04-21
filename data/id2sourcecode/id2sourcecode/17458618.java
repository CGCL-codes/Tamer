    public String toDescription() {
        String result = "";
        int water = (((WaterMaxSpots + WaterMinSpots) / 2) * (WaterMinHexes + WaterMaxSpots) / 2) + RiverProb / 10;
        int rough = (((RoughMaxSpots + RoughMinSpots) / 2) * (RoughMinHexes + RoughMaxSpots) / 2);
        int forest = (((ForestMaxSpots + ForestMinSpots) / 2) * (ForestMinHexes + ForestMaxSpots) / 2);
        result += "The landscape is ";
        if (Hillyness < 200) result += "plain";
        if ((Hillyness < 500) && (Hillyness >= 200)) result += "uneven";
        if ((Hillyness >= 500) && (Hillyness <= 800)) result += "hilly";
        if (Hillyness > 800) result += "mountainous";
        if (CraterProb == 0) {
            result += ". <br> ";
            if (rough > 0) {
                result += "Through tectonic activity of this continent, rough terrain is appearing";
                if (rough > 8) result += " everywhere"; else result += " sometimes";
            }
        } else {
            if (CraterProb < 30) result += ", which is seldom coverd with";
            if ((CraterProb >= 30) && (CraterProb < 60)) result += ", which is covered with";
            if (CraterProb >= 60) result += ", often coverd with";
            int avgCraterSize = (CraterMinRadius + CraterMaxRadius) / 2;
            if (avgCraterSize < 4) result += " small craters";
            if ((avgCraterSize >= 4) && (avgCraterSize < 7)) result += " craters ";
            if (avgCraterSize >= 7) result += " large craters";
            if (rough > 0) {
                result += ". Another remaing of the ancient meteorid impacts is the rough terrain appearing";
                if (rough > 8) result += " everywhere"; else result += " sometimes";
            }
        }
        result += ". <br>";
        result += "Most facitlities on this continent are lying";
        if (forest > 50) {
            result += " deep in the ";
            result += (ForestHeavyProb < 30) ? "woods" : "jungle";
            if (water > 20) result += " mixed up with much water, because of heavy rain due too monsoon period";
            result += ".";
        } else {
            if (water > 20) result += " close to the coast."; else if (water < 3) {
                if (forest < 15) result += " in the desert. So dont expect vegetation for cover or water for cooling."; else result += " in an area moderatly forested.";
            } else result += " in an area famous for its agriculture.";
        }
        return result;
    }

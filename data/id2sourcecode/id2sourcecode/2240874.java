    public void saveConfig() {
        try {
            FileWriter fw;
            BufferedWriter bw;
            String path = new java.io.File(".").getCanonicalPath() + "\\src\\config\\" + configName + ".config";
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            Element configInfoNode = existedMap(configName);
            if (configInfoNode != null) {
                int option = JOptionPane.showOptionDialog(null, "This file already exists. Overwrite it?", "NOTIFICATION", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (option == JOptionPane.YES_OPTION) {
                    root.removeChild(configInfoNode);
                    fw = new FileWriter(path, false);
                } else {
                    return;
                }
            }
            fw = new FileWriter(path, true);
            bw = new BufferedWriter(fw);
            write(bw, true, "");
            write(bw, true, "Document :\t" + configName + ".config");
            write(bw, true, "Created on :\t" + dateFormat.format(date));
            write(bw, true, "");
            write(bw, false, "");
            write(bw, false, "");
            write(bw, false, "");
            configInfoNode = doc.createElement("config");
            configInfoNode.setAttribute("name", configName);
            root.appendChild(configInfoNode);
            Element configOptionNode;
            Element temp1;
            Element temp2;
            write(bw, true, "");
            write(bw, true, "GENERAL CONFIGURATION");
            write(bw, true, "");
            write(bw, false, "");
            configOptionNode = doc.createElement("general");
            write(bw, true, "Port communication offset");
            write(bw, false, "commOffset = " + GUIOptionManager.getCommOffset());
            write(bw, false, "");
            configOptionNode.setAttribute("commOffset", "" + GUIOptionManager.getCommOffset());
            write(bw, true, "Total number of nodes");
            write(bw, false, "totalNodes = " + GUIOptionManager.getTotalNodes());
            write(bw, false, "");
            configOptionNode.setAttribute("totalNodes", "" + GUIOptionManager.getTotalNodes());
            write(bw, true, "Simulation time (min)");
            write(bw, false, "simTimeMin = " + GUIOptionManager.getSimTimeMin());
            write(bw, false, "");
            configOptionNode.setAttribute("simTimeMin", "" + GUIOptionManager.getSimTimeMin());
            write(bw, true, "Simulation time (sec)");
            write(bw, false, "simTimeSec = " + GUIOptionManager.getSimTimeSec());
            write(bw, false, "");
            configOptionNode.setAttribute("simTimeSec", "" + GUIOptionManager.getSimTimeSec());
            write(bw, true, "Total simulation time (sec)");
            write(bw, false, "simTotalTimeInSec = " + GUIOptionManager.getSimTotalTimeInSec());
            write(bw, false, "");
            configOptionNode.setAttribute("simTotalTimeInSec", "" + GUIOptionManager.getSimTotalTimeInSec());
            write(bw, true, "Frequency used");
            write(bw, false, "frequency = " + GUIOptionManager.getFrequency());
            write(bw, false, "");
            configOptionNode.setAttribute("frequency", "" + GUIOptionManager.getFrequency());
            write(bw, true, "Time scaling (1 sec simulation time correspondency with field time in sec)");
            write(bw, false, "timeScale = " + GUIOptionManager.getTimeScale());
            write(bw, false, "");
            configOptionNode.setAttribute("timeScale", "" + GUIOptionManager.getTimeScale());
            write(bw, true, "Time passed for tranception in field time (sec)");
            write(bw, false, "timeScaleTranception = " + GUIOptionManager.getTimeScaleTranception());
            write(bw, false, "");
            configOptionNode.setAttribute("timeScaleTranception", "" + GUIOptionManager.getTimeScaleTranception());
            write(bw, true, "Time passed for processing in field time (sec)");
            write(bw, false, "timeScaleProcessing = " + GUIOptionManager.getTimeScaleProcessing());
            write(bw, false, "");
            configOptionNode.setAttribute("timeScaleProcessing", "" + GUIOptionManager.getTimeScaleProcessing());
            write(bw, true, "Broker is simulated in indicated IP");
            write(bw, false, "ip = " + GUIOptionManager.getIp());
            write(bw, false, "");
            configOptionNode.setAttribute("ip", GUIOptionManager.getIp());
            configInfoNode.appendChild(configOptionNode);
            write(bw, true, "");
            write(bw, true, "TERRAIN CONFIGURATION");
            write(bw, true, "");
            write(bw, false, "");
            configOptionNode = doc.createElement("terrain");
            write(bw, true, "Name of the map used");
            write(bw, false, "mapName = " + GUIOptionManager.getMapName());
            write(bw, false, "");
            configOptionNode.setAttribute("mapName", "" + GUIOptionManager.getMapName());
            write(bw, true, "Map's width (meters)");
            write(bw, false, "mapWidthMeters = " + GUIOptionManager.getMapWidthMeters());
            write(bw, false, "");
            configOptionNode.setAttribute("mapWidthMeters", "" + GUIOptionManager.getMapWidthMeters());
            write(bw, true, "Map's height (meters)");
            write(bw, false, "mapHeightMeters = " + GUIOptionManager.getMapHeightMeters());
            write(bw, false, "");
            configOptionNode.setAttribute("mapHeightMeters", "" + GUIOptionManager.getMapHeightMeters());
            write(bw, true, "Map's fragmentation width (cells)");
            write(bw, false, "mapWidthObstacles = " + GUIOptionManager.getMapWidthObstacles());
            write(bw, false, "");
            configOptionNode.setAttribute("mapWidthObstacles", "" + GUIOptionManager.getMapWidthObstacles());
            write(bw, true, "Map's fragmentation height (cells)");
            write(bw, false, "mapHeightObstacles = " + GUIOptionManager.getMapHeightObstacles());
            write(bw, false, "");
            configOptionNode.setAttribute("mapHeightObstacles", "" + GUIOptionManager.getMapHeightObstacles());
            temp1 = doc.createElement("obstacles");
            if (GUIOptionManager.getObstacles() != null) {
                write(bw, true, "Total obstacles : " + GUIOptionManager.getObstacles().size());
                temp1.setAttribute("total", "" + GUIOptionManager.getObstacles().size());
                for (int i = 0; i < GUIOptionManager.getObstacles().size(); i++) {
                    DrawingBean temp = GUIOptionManager.getObstacles().get(i);
                    write(bw, false, "\tposition = " + temp.getPoint().toString().substring(temp.getPoint().toString().indexOf("[")) + "\tcolor = " + temp.getColor().toString().substring(temp.getColor().toString().indexOf("[")) + "\tinfo = " + temp.getInfo());
                    temp2 = doc.createElement("obstacle");
                    temp2.setAttribute("info", temp.getInfo());
                    temp2.setAttribute("color", "" + temp.getColor());
                    temp2.setAttribute("point", "" + temp.getPoint());
                    temp1.appendChild(temp2);
                }
                write(bw, false, "");
            } else {
                write(bw, true, "Total obstacles : 0");
                write(bw, false, "");
                temp1.setAttribute("total", "" + 0);
            }
            configOptionNode.appendChild(temp1);
            configInfoNode.appendChild(configOptionNode);
            write(bw, true, "");
            write(bw, true, "NODES CONFIGURATION");
            write(bw, true, "");
            write(bw, false, "");
            configOptionNode = doc.createElement("nodes");
            write(bw, true, "- Node definition Section -");
            write(bw, true, "Number of different node categories");
            write(bw, false, "nodeCategories = " + GUIOptionManager.getNodeCategories());
            write(bw, false, "");
            configOptionNode.setAttribute("nodeCategories", "" + GUIOptionManager.getNodeCategories());
            temp1 = doc.createElement("nodeArchPerCategory");
            if (GUIOptionManager.getNodeArchPerCategory() != null) {
                write(bw, true, "Total categories : " + GUIOptionManager.getNodeArchPerCategory().size());
                temp1.setAttribute("total", "" + GUIOptionManager.getNodeArchPerCategory().size());
                for (int i = 0; i < GUIOptionManager.getNodeArchPerCategory().size(); i++) {
                    NodeArchBean temp = GUIOptionManager.getNodeArchPerCategory().get(i);
                    write(bw, false, "\tnumberOfNodes = " + temp.getNumberOfNodes() + "\tenergy_mWh = " + temp.getEnergy_mWh() + "\tmobile = " + temp.isMobile() + "\tenConsIdle = " + temp.getEnConsIdle() + "\tenConsMove = " + temp.getEnConsMove() + "\tenConsReceive = " + temp.getEnConsReceive() + "\tenConsTransmit = " + temp.getEnConsTransmit() + "\tenConsProcess = " + temp.getEnConsProcess() + "\tdaemonIntervalInSec = " + temp.getDaemonIntervalInSec() + "\tantennaRadPow = " + temp.getAntennaRadPow() + "\tantennaThres = " + temp.getAntennaThres());
                    temp2 = doc.createElement("category");
                    temp2.setAttribute("numberOfNodes", "" + temp.getNumberOfNodes());
                    temp2.setAttribute("energy_mWh", "" + temp.getEnergy_mWh());
                    temp2.setAttribute("mobile", "" + temp.isMobile());
                    temp2.setAttribute("enConsIdle", "" + temp.getEnConsIdle());
                    temp2.setAttribute("enConsMove", "" + temp.getEnConsMove());
                    temp2.setAttribute("enConsReceive", "" + temp.getEnConsReceive());
                    temp2.setAttribute("enConsTransmit", "" + temp.getEnConsTransmit());
                    temp2.setAttribute("enConsProcess", "" + temp.getEnConsProcess());
                    temp2.setAttribute("daemonIntervalInSec", "" + temp.getDaemonIntervalInSec());
                    temp2.setAttribute("antennaRadPow", "" + temp.getAntennaRadPow());
                    temp2.setAttribute("antennaThres", "" + temp.getAntennaThres());
                    temp1.appendChild(temp2);
                }
                write(bw, false, "");
            } else {
                write(bw, true, "Total categories : 0");
                write(bw, false, "");
                temp1.setAttribute("total", "" + 0);
            }
            configOptionNode.appendChild(temp1);
            temp1 = doc.createElement("nodes");
            if (GUIOptionManager.getNodes() != null) {
                write(bw, true, "Total nodes : " + GUIOptionManager.getNodeArchPerCategory().size());
                temp1.setAttribute("total", "" + GUIOptionManager.getNodes().size());
                for (int i = 0; i < GUIOptionManager.getNodes().size(); i++) {
                    DrawingBean temp = GUIOptionManager.getNodes().get(i);
                    write(bw, false, "\tposition = " + temp.getPoint().toString().substring(temp.getPoint().toString().indexOf("[")) + "\tcolor = " + temp.getColor().toString().substring(temp.getColor().toString().indexOf("[")) + "\tinfo = " + temp.getInfo());
                    temp2 = doc.createElement("node");
                    temp2.setAttribute("info", temp.getInfo());
                    temp2.setAttribute("color", "" + temp.getColor());
                    temp2.setAttribute("point", "" + temp.getPoint());
                    temp1.appendChild(temp2);
                }
                write(bw, false, "");
            } else {
                write(bw, true, "Total nodes : 0");
                write(bw, false, "");
                temp1.setAttribute("total", "" + 0);
            }
            configOptionNode.appendChild(temp1);
            write(bw, true, "- Mobility Section -");
            temp1 = doc.createElement("mobility");
            write(bw, true, "Mobility model used (Random Waypoint / Reference Point Group Mobility)");
            write(bw, false, "rpgm = " + GUIOptionManager.isRpgm());
            write(bw, false, "");
            temp1.setAttribute("rpgm", "" + GUIOptionManager.isRpgm());
            write(bw, true, "RPGM submodel used (In-place / Overlap)");
            write(bw, false, "inPlace = " + GUIOptionManager.isInplace());
            write(bw, false, "");
            temp1.setAttribute("inPlace", "" + GUIOptionManager.isInplace());
            write(bw, true, "Maximum velocity while moving");
            write(bw, false, "maxVelocity = " + GUIOptionManager.getMaxVelocity());
            write(bw, false, "");
            temp1.setAttribute("maxVelocity", "" + GUIOptionManager.getMaxVelocity());
            write(bw, true, "Grid used in RPGM model");
            write(bw, false, "grid = " + GUIOptionManager.getGrid());
            write(bw, false, "");
            temp1.setAttribute("grid", "" + GUIOptionManager.getGrid());
            configOptionNode.appendChild(temp1);
            configInfoNode.appendChild(configOptionNode);
            write(bw, true, "");
            write(bw, true, "RADIO PROPAGATION CONFIGURATION");
            write(bw, true, "");
            write(bw, false, "");
            write(bw, true, "- Rain Section -");
            configOptionNode = doc.createElement("radioPropagation");
            temp1 = doc.createElement("rain");
            if (GUIOptionManager.getRain() != null) {
                write(bw, true, "Total rain sectors : " + GUIOptionManager.getRain().size());
                temp1.setAttribute("total", "" + GUIOptionManager.getRain().size());
                for (int i = 0; i < GUIOptionManager.getRain().size(); i++) {
                    DrawingBean temp = GUIOptionManager.getRain().get(i);
                    write(bw, false, "\tposition = " + temp.getPoint().toString().substring(temp.getPoint().toString().indexOf("[")) + "\tcolor = " + temp.getColor().toString().substring(temp.getColor().toString().indexOf("[")) + "\tinfo = " + temp.getInfo());
                    temp2 = doc.createElement("drop");
                    temp2.setAttribute("info", temp.getInfo());
                    temp2.setAttribute("color", "" + temp.getColor());
                    temp2.setAttribute("point", "" + temp.getPoint());
                    temp1.appendChild(temp2);
                }
                write(bw, false, "");
            } else {
                write(bw, true, "Total rain sectors : 0");
                write(bw, false, "");
                temp1.setAttribute("total", "" + 0);
            }
            configOptionNode.appendChild(temp1);
            configOptionNode.setAttribute("heavyAttenuation", "" + GUIOptionManager.getHeavyAttenuation());
            configOptionNode.setAttribute("lightAttenuation", "" + GUIOptionManager.getLightAttenuation());
            configOptionNode.setAttribute("mapWidthRain", "" + GUIOptionManager.getMapWidthRain());
            configOptionNode.setAttribute("mapHeightRain", "" + GUIOptionManager.getMapHeightRain());
            configInfoNode.appendChild(configOptionNode);
            bw.close();
            processor.writeXML(doc);
        } catch (IOException ex) {
            Logger.getLogger(ConfigXMLManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void saveGame(J3DCore core, String slotName, InitCallbackObject callbackObject) {
        if (core.gameLost) {
            core.uiBase.hud.mainBox.addEntry("Cannot save lost game.");
            return;
        }
        try {
            core.busyPane.setToType(BusyPaneWindow.LOADING, "Saving...");
            core.busyPane.show();
            core.drawForced();
            Date d = new Date();
            String dT = new SimpleDateFormat("yyyyMMdd-HHmmss.SSS").format(d);
            String slot = saveDir + "/" + core.gameState.gameId + "_" + dT + "/";
            File f = new File(slot);
            f.mkdirs();
            File desc = new File(slot + "desc.txt");
            FileWriter fw = new FileWriter(desc);
            String dT2 = new SimpleDateFormat("yyyy.MM.dd HH:mm").format(d) + " Seed:" + core.gameState.scenarioDesc.seed;
            fw.write((slotName != null && slotName.length() > 15 ? slotName.substring(0, 15) : slotName) + "\n(" + dT2 + ")");
            fw.close();
            File saveGame = new File(slot + "savegame.zip");
            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(saveGame));
            zipOutputStream.putNextEntry(new ZipEntry("gamestate.xml"));
            long time = System.currentTimeMillis();
            core.gameState.getGameStateXml(zipOutputStream);
            System.out.println("][][][][][][ SAVE TIME = " + (System.currentTimeMillis() - time));
            zipOutputStream.close();
            core.busyPane.hide();
            core.drawForced();
            try {
                ScreenShotImageExporter _screenShotExp = new ScreenShotImageExporter(new File(slot), "screen", "jpg", false);
                core.screenshotControlled(_screenShotExp, callbackObject);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            core.uiBase.hud.mainBox.addEntry("Game saved.");
        } catch (Exception ex) {
            core.busyPane.hide();
            ex.printStackTrace();
        }
    }

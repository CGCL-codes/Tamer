    public void load(URL levelURL) {
        removeAllEntities();
        Research.reset();
        Player.HUMAN.credits = 0;
        if (SelectedPlanet.instance != null) if (SelectedPlanet.instance != null && SelectedPlanet.getInstance(main, null).getParent() != null) SelectedPlanet.getInstance(main, null).close();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            InputStream is = levelURL.openStream();
            int b = 0;
            while ((b = is.read()) != -1) baos.write(b);
            is.close();
            String buffer = baos.toString();
            String[] lines = buffer.split("\n");
            for (String line : lines) {
                String[] meta_and_pos_and_name = line.split("#");
                String meta = meta_and_pos_and_name[0];
                String pos = meta_and_pos_and_name[1];
                String name = "";
                if (meta_and_pos_and_name.length > 2) name = meta_and_pos_and_name[2];
                String[] xyz = pos.split(":");
                float x = Float.parseFloat(xyz[0]);
                float y = Float.parseFloat(xyz[1]);
                float z = Float.parseFloat(xyz[2]);
                String[] type_and_count_and_radius = meta.split(":");
                Player player = Player.valueOf(type_and_count_and_radius[0]);
                int count = Integer.parseInt(type_and_count_and_radius[1]);
                int radius = Integer.parseInt(type_and_count_and_radius[2]);
                if (name.equals("Camera")) {
                    try {
                        Planet humanPlanet = Player.HUMAN.getPlanets().get(0);
                        Vector3 newDirection = new Vector3();
                        main.cameraTranslationNode.setTranslation(0, 0, newDirection.set(x, y, z).subtractLocal(humanPlanet.getPosition()).length());
                        newDirection.set(x, y, z).subtractLocal(humanPlanet.getPosition()).normalizeLocal();
                        main.cameraRotationNode.setTranslation(humanPlanet.getPosition());
                        boolean matrix3LookAt = false;
                        if (matrix3LookAt) {
                            ((Matrix3) main.cameraRotationNode.getRotation()).lookAt(newDirection, main.worldUp);
                            main.cameraRotationNode.markDirty(DirtyType.Transform);
                        } else {
                            Quaternion transformAngle = new Quaternion();
                            transformAngle.lookAt(newDirection, main.worldUp);
                            main.cameraRotationNode.setRotation(transformAngle);
                            transformAngle.lookAt(Vector3.NEG_UNIT_Z, main.worldUp);
                            main.cameraTranslationNode.setRotation(transformAngle);
                        }
                        main.updateListenerPosition();
                    } catch (Exception e) {
                        System.out.println("camera planet must be in level file after a human planet!");
                        System.out.println(e.getMessage());
                    }
                    continue;
                }
                Planet p = null;
                switch(player) {
                    case HUMAN:
                        p = new Planet(main, Player.HUMAN, name, new Vector3(x, y, z), radius / 20d);
                        this.getNode().attachChild(p.getNode());
                        break;
                    case COMPUTER:
                        p = new Planet(main, Player.COMPUTER, name, new Vector3(x, y, z), radius / 20d);
                        this.getNode().attachChild(p.getNode());
                        break;
                    case NEUTRAL:
                        p = new Planet(main, Player.NEUTRAL, name, new Vector3(x, y, z), radius / 20d);
                        this.getNode().attachChild(p.getNode());
                        break;
                }
                for (int i = 0; i < count; i++) {
                    p.spawnNewFighter();
                }
            }
            for (Planet s : Planet.planets) {
                for (Planet s2 : Planet.planets) {
                    double distanceSquared = s.getPosition().distanceSquared(s2.getPosition());
                    if (distanceSquared < Settings.MAX_TRAVEL_DISTANCE_SQUARED) {
                        Entities.connect(s, s2);
                        if (Settings.showPlanetConnectingLines) {
                            Line line = CoolLine.get(s.getPosition(), s2.getPosition());
                            line.setName("path");
                            main.root.attachChild(line);
                        }
                    }
                }
            }
            Planet humanPlanet = Player.HUMAN.getPlanets().get(0);
            if (humanPlanet != null) {
                main.lookHere = new Vector3(Vector3.NEG_UNIT_X);
                main.cameraRotationNode.setTranslation(humanPlanet.getPosition());
            }
            main.timer.reset();
            Laser.lastTime = 0;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

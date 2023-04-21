    private void setup() {
        env = new EnvJython();
        pythonInterpreter = new PythonInterpreter(dict);
        pythonInterpreter.set("env", env);
        try {
            script = pythonInterpreter.compile(FileUtils.readFileToString(new File("World")));
            pythonInterpreter.exec(script);
            setupMethod = pythonInterpreter.get("setup");
            moveMethod = pythonInterpreter.get("move");
        } catch (FileNotFoundException e) {
            script = null;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        try {
            URL url = Sysutil.getURL("world.env");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[0].equalsIgnoreCase("skybox")) {
                    env.setRoom(new EnvSkyRoom(fields[1]));
                } else if (fields[0].equalsIgnoreCase("camera")) {
                    env.setCameraXYZ(Double.parseDouble(fields[1]), Double.parseDouble(fields[2]), Double.parseDouble(fields[3]));
                    env.setCameraYaw(Double.parseDouble(fields[4]));
                    env.setCameraPitch(Double.parseDouble(fields[5]));
                } else if (fields[0].equalsIgnoreCase("terrain")) {
                    terrain = new EnvTerrain(fields[1]);
                    terrain.setTextureAlpha(fields[2]);
                    terrain.setTexture(fields[3], 1);
                    terrain.setTexture(fields[4], 2);
                    terrain.setTexture(fields[5], 3);
                    env.addObject(terrain);
                } else if (fields[0].equalsIgnoreCase("object")) {
                    GameObject n = new GameObject(fields[10]);
                    n.setX(Double.parseDouble(fields[1]));
                    n.setY(Double.parseDouble(fields[2]));
                    n.setZ(Double.parseDouble(fields[3]));
                    n.setScale(Double.parseDouble(fields[4]));
                    n.setRotateX(Double.parseDouble(fields[5]));
                    n.setRotateY(Double.parseDouble(fields[6]));
                    n.setRotateZ(Double.parseDouble(fields[7]));
                    n.setTexture(fields[9]);
                    n.setModel(fields[8]);
                    env.addObject(n);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

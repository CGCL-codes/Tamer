    public void run() {
        try {
            while (running) {
                Thread.sleep(1000);
                if (!running) break;
                boolean notify = false;
                synchronized (this) {
                    URLConnection c = new URL(url + "&after=" + lastID).openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream(), "ISO-8859-1"));
                    String line;
                    int maxID = 0;
                    while ((line = br.readLine()) != null) {
                        if (line.startsWith("::")) {
                            line = line.substring(2);
                            int pos = line.indexOf(":");
                            if (pos == -1) {
                                System.out.println("!> " + line);
                                continue;
                            }
                            try {
                                int id = Integer.parseInt(line.substring(0, pos));
                                if (id > lastID) {
                                    if (id > maxID) maxID = id;
                                    String shape = line.substring(pos + 1);
                                    if (os.add(shape)) notify = true;
                                }
                            } catch (NumberFormatException ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            System.out.println(">>" + line);
                        }
                    }
                    if (maxID > lastID) lastID = maxID;
                    br.close();
                    br = null;
                    c = null;
                }
                if (notify) {
                    notifyWhiteboard();
                }
                System.gc();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

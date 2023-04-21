        public void run() {
            String ts = null;
            long millis;
            String savename;
            FileOutputStream file;
            boolean myLocalLogging = false;
            if (shouldSave) {
                try {
                    setADEServerLogging(true);
                    myLocalLogging = true;
                } catch (ADETimeoutException ate) {
                    System.err.println(prg + " ATE: " + ate);
                } catch (ADECallException ace) {
                    System.err.println(prg + " ACE: " + ace);
                } catch (AccessControlException access) {
                    System.err.println(prg + " ACCESS: " + access);
                } catch (Exception e) {
                    System.err.println(prg + ": unable to start logging");
                    System.err.println(prg + ": " + e);
                }
            }
            while (shouldRecord) {
                try {
                    while ((ais.available() < bytesWaitFor) && shouldRecord) {
                        Sleep(sampleSleep);
                    }
                    synchronized (ais) {
                        audioread = ais.read(abytes, 0, bytesWaitFor);
                        newbytes = true;
                    }
                    if (playAudio) outLine.write(abytes, 0, audioread);
                    if (shouldSave) {
                        try {
                            ts = logIt("Audio frame");
                        } catch (IOException ioe2) {
                            System.err.println(prg + ": error logging");
                        }
                        savename = "logs/ar_" + saveName + "_" + ts + ".raw";
                        System.out.println("saving " + savename);
                        file = new FileOutputStream(savename);
                        file.write(abytes, 0, audioread);
                        file.close();
                    }
                    if (outFile != null) pout.write(abytes, 0, audioread);
                } catch (IOException ioe) {
                    System.out.println(prg + ": Error accessing ais: " + ioe);
                }
            }
        }

    private void update(Map<String, IpEntry> ipMap) {
        if (denyFile == null) {
            log.error("DenyFile not set");
            return;
        }
        log.info("Saving " + denyFile);
        try {
            BufferedReader template = new BufferedReader(new InputStreamReader(Util.getStream("/hosts.deny")));
            BufferedWriter output = new BufferedWriter(new FileWriter(denyFile));
            String line = null;
            while ((line = template.readLine()) != null) output.write(line + "\n");
            output.write("ALL:");
            for (String ip : ipMap.keySet()) output.write("\\\n    " + ip);
            output.write("\n\n");
            template.close();
            output.close();
            log.debug("Saved succesfully");
        } catch (IOException e) {
            log.error("Error during update", e);
        }
    }

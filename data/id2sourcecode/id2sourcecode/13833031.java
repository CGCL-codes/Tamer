    private BufferedReader sendNetworkToServer(String raw_fname, String xgmml_fname) throws IOException, Exception {
        String data = "proteins=";
        String data_prot = "";
        LinkedList<String> network = getNetwork();
        if (node_count > 1000) taskMonitor.setStatus("Your network is over 1000 nodes. " + "Predicting over such a large data set can take quite a bit of time. ");
        int cv = 0;
        while (cv < network.size()) {
            data_prot += network.get(cv) + '\n';
            ++cv;
        }
        try {
            data += URLEncoder.encode(data_prot, "UTF-8");
        } catch (UnsupportedEncodingException uee) {
            throw new Exception("Error with using URLEncoder");
        }
        if (alg_buttons != null) data += "&algorithm=" + alg_buttons.getSelection().getActionCommand(); else data += "&algorithm=mssc";
        try {
            System.out.println("Cytoprophet is now querying the server to predict interactions...");
            URL url = new URL("http://ppi.cse.nd.edu:8080/examples/servlets/servlet/Prediction");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            System.out.println("Connected to the Cytoprophet server...");
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            wr.close();
            if (node_count > 1000) taskMonitor.setStatus("Request sent to server... " + "Your network contains over 1000 nodes. " + "Predicting over such a large data set can take a long time.");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            BufferedWriter out_raw, out_ppi, out_ddi, out_go, out_alias;
            out_raw = new BufferedWriter(new FileWriter(tmp_fpath + runNum + "server_raw.sif"));
            out_ppi = new BufferedWriter(new FileWriter(tmp_fpath + runNum + "server.ppi"));
            out_ddi = new BufferedWriter(new FileWriter(tmp_fpath + runNum + "serverddi.sif"));
            out_go = new BufferedWriter(new FileWriter(tmp_fpath + runNum + "server.go"));
            out_alias = new BufferedWriter(new FileWriter(tmp_fpath + runNum + "server.alias"));
            if (interrupted) return rd;
            System.out.println("Receiving server data...");
            while ((line = rd.readLine()) != null) {
                if (line.contains("<START PPI>")) break;
                out_raw.write(line + '\n');
            }
            out_raw.close();
            if (interrupted) return rd;
            while ((line = rd.readLine()) != null) {
                if (line.contains("<START DDI>")) break;
                out_ppi.write(line + '\n');
            }
            out_ppi.close();
            if (interrupted) return rd;
            while ((line = rd.readLine()) != null) {
                if (line.contains("<START GO>")) break;
                out_ddi.write(line + '\n');
            }
            out_ddi.close();
            if (interrupted) return rd;
            while ((line = rd.readLine()) != null) {
                if (line.contains("<START ALIAS>")) break;
                out_go.write(line + '\n');
            }
            out_go.close();
            if (interrupted) return rd;
            while ((line = rd.readLine()) != null) {
                out_alias.write(line + '\n');
            }
            out_alias.close();
            return rd;
        } catch (IOException ex) {
            throw new Exception("Error in server");
        }
    }

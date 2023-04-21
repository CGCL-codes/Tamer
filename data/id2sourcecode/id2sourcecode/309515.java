    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == exitBTN) {
            System.exit(0);
        } else if (source == gatherInfoBTN) {
            RegQuery query = new RegQuery();
            infoTA.setText("Personal Directory: " + query.getCurrentUserPersonalFolderPath() + "\n");
            infoTA.append("CPU NAME: " + query.getCPUName() + "\n");
            infoTA.append("CPU Speed: " + query.getCPUSpeed() + " MHz" + "\n");
            infoTA.append("Physical Memory: " + query.getMem() + " MB" + "\n");
            infoTA.append("MAC Address: " + query.getMAC() + "\n");
            infoTA.append("IP Address: " + query.getIP() + "\n");
            infoTA.append("Mouse: " + query.getMouse() + "\n");
            infoTA.append("Keyboard: " + query.getKeyboard() + "\n");
            infoTA.append("OS Name: " + query.getOSName() + "\n");
            System.out.println(query.getOSName());
            infoTA.append("Product ID: " + query.getProductID() + "\n");
            infoTA.append("Registered Owner: " + query.getOwner() + "\n");
            infoTA.append("Windows Activation Key: " + query.getProductKey() + "\n");
            infoTA.append("Service Pack Number: " + query.getServicePack() + "\n");
            infoTA.append("Computer Name: " + query.getComputerName() + "\n");
            infoTA.append("Default User Name: " + query.getUserName() + "\n");
            infoTA.append("BIOS Version: " + query.getBios() + "\n");
            infoTA.append("Modem: " + query.getModem() + "\n");
            infoTA.append("Hard Disk 1: " + query.getHDD() + "\n");
            infoTA.append("SCSI Disk 1: " + query.getSDD1() + "\n");
            infoTA.append("SCSI Disk 2: " + query.getSDD2() + "\n");
            infoTA.append("Parallel Port: " + query.getLPT() + "\n");
            infoTA.append("Com1 Port: " + query.getCOM1() + "\n");
            infoTA.append("Com3 Port: " + query.getCOM3() + "\n");
            infoTA.append("1394 Firewire: " + query.get1394() + "\n");
            if (sendInfoCB.getState()) {
                String data = "";
                try {
                    data = URLEncoder.encode("Enter", "UTF-8") + "=" + URLEncoder.encode("Enter", "UTF-8");
                    data += URLEncoder.encode("cpu_type", "UTF-8") + "=" + URLEncoder.encode(query.getCPUName(), "UTF-8");
                    data += "&" + URLEncoder.encode("cpu_speed", "UTF-8") + "=" + URLEncoder.encode(query.getCPUSpeed(), "UTF-8");
                    data += "&" + URLEncoder.encode("ip", "UTF-8") + "=" + URLEncoder.encode(query.getIP(), "UTF-8");
                    data += "&" + URLEncoder.encode("mouse", "UTF-8") + "=" + URLEncoder.encode(query.getMouse(), "UTF-8");
                    data += "&" + URLEncoder.encode("keyboard", "UTF-8") + "=" + URLEncoder.encode(query.getKeyboard(), "UTF-8");
                    data += "&" + URLEncoder.encode("os", "UTF-8") + "=" + URLEncoder.encode(query.getOSName(), "UTF-8");
                    data += "&" + URLEncoder.encode("os_sn", "UTF-8") + "=" + URLEncoder.encode(query.getProductKey(), "UTF-8");
                    data += "&" + URLEncoder.encode("owner", "UTF-8") + "=" + URLEncoder.encode(query.getOwner(), "UTF-8");
                    data += "&" + URLEncoder.encode("hostname", "UTF-8") + "=" + URLEncoder.encode(query.getComputerName(), "UTF-8");
                    data += "&" + URLEncoder.encode("modem", "UTF-8") + "=" + URLEncoder.encode(query.getModem(), "UTF-8");
                    data += "&" + URLEncoder.encode("hd0", "UTF-8") + "=" + URLEncoder.encode(query.getHDD(), "UTF-8");
                    data += "&" + URLEncoder.encode("lpt1", "UTF-8") + "=" + URLEncoder.encode(query.getLPT(), "UTF-8");
                    data += "&" + URLEncoder.encode("com1", "UTF-8") + "=" + URLEncoder.encode(query.getCOM1(), "UTF-8");
                    data += "&" + URLEncoder.encode("ieee_1394", "UTF-8") + "=" + URLEncoder.encode(query.get1394(), "UTF-8");
                    URL url = new URL("http://192.168.0.41:80/cdm_data_entry.php");
                    URLConnection conn = url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(data);
                    wr.flush();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line = "";
                    while ((line = rd.readLine()) != null) {
                    }
                    wr.close();
                    rd.close();
                } catch (Exception ex) {
                }
            }
        }
    }

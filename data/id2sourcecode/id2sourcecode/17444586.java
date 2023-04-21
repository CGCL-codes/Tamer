    public void setAction() {
        loadbutton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                resultsdatabase = doc.wireresultsdatabase;
                refreshTable();
            }
        });
        bcmbutton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String bcmchoice = new String("RTBT_Diag:BCM25");
                DecimalFormat decfor = new DecimalFormat("0.###E0");
                double charge = 0.0;
                Channel bcmChargeCh = ChannelFactory.defaultFactory().getChannel(bcmchoice + ":Q");
                try {
                    charge = Math.abs(bcmChargeCh.getValDbl());
                } catch (ConnectionException ce) {
                    System.out.println("Cannot connect to " + bcmChargeCh.getId());
                } catch (GetException ce) {
                    System.out.println("Cannot get value from " + bcmChargeCh.getId());
                }
                Channel.flushIO();
                ppp = charge / 1.602e-19;
                Np.setText((String) decfor.format(ppp));
                System.out.println("Charge is " + charge + " " + ppp);
                doc.charge = ppp;
            }
        });
        solvebutton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                HashMap beamarearatios = doc.beamarearatios;
                HashMap windowarearatios = doc.windowarearatios;
                DecimalFormat decfor = new DecimalFormat("0.###E0");
                int nrows = datatablemodel.getRowCount();
                double xroot = 0.0;
                double Gx = 0.0;
                double yroot = 0.0;
                double Gy = 0.0;
                double rho_wire = 0.0;
                double areafac = 1.0;
                double wareafac = 1.0;
                double rho_target = 0.0;
                double rho_window = 0.0;
                double np = 0.0;
                double avg_rho_target = 0.0;
                double avg_rho_window = 0.0;
                double count = 0.0;
                double peakfac = 0.0;
                double sumdensity = 0.0;
                Double N = new Double(0.0);
                np = N.parseDouble((String) Np.getText());
                System.out.println("Number of protons is " + np);
                doc.charge = np;
                avg_rho_target = 0.0;
                avg_rho_window = 0.0;
                count = 0.0;
                for (int i = 0; i < nrows; i++) {
                    if ((Boolean) datatable.getValueAt(i, 1) == true) {
                        String label = (String) datatable.getValueAt(i, 0);
                        xroot = getMaxima(label, new String("H"));
                        Gx = getDensity(xroot, label, new String("H"));
                        yroot = getMaxima(label, new String("V"));
                        Gy = getDensity(yroot, label, new String("V"));
                        rho_wire = np * Gy * Gx;
                        System.out.println("xroot is " + xroot + "; yroot is " + yroot);
                        System.out.println("Gx = " + Gx + " Gy = " + Gy + " Gx*Gy*np " + rho_wire);
                        areafac = ((Double) beamarearatios.get(label)).doubleValue();
                        wareafac = ((Double) windowarearatios.get(label)).doubleValue();
                        rho_target = rho_wire * areafac * 0.96;
                        rho_window = rho_wire * wareafac;
                        peakfac = rho_target / (1.25e-4) / np;
                        sumdensity += rho_target;
                        System.out.println("For " + label + " tareafac is  " + areafac);
                        System.out.println("For " + label + " wareafac is  " + wareafac);
                        System.out.println("x, Gx, y, Gy are = " + xroot + " " + Gx + " " + yroot + " " + Gy);
                        datatablemodel.setValueAt(new String(decfor.format(rho_wire)), i, 2);
                        datatablemodel.setValueAt(new String(decfor.format(rho_window)), i, 3);
                        datatablemodel.setValueAt(new String(decfor.format(rho_target)), i, 4);
                        datatablemodel.setValueAt(new String(decfor.format(peakfac)), i, 5);
                        avg_rho_target += rho_target;
                        avg_rho_window += rho_window;
                        count += 1.0;
                    }
                }
                avg_rho_target /= count;
                avg_rho_window /= count;
                double avedensity = sumdensity /= count;
                avgdensity.setText((String) decfor.format(avedensity));
                System.out.println("target ave is " + avg_rho_target);
                doc.tdensity = avg_rho_target;
                doc.wdensity = avg_rho_window;
                datatablemodel.fireTableDataChanged();
            }
        });
    }

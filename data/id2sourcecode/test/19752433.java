        Panel() {
            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            JLabel label = new JLabel("center position:");
            JLabel labelLon = new JLabel("lon:");
            JLabel labelLat = new JLabel("lat:");
            List<JButton> buttons = new ArrayList<JButton>();
            JButton buttonOsmWeb = new UrlButton("Openstreetmap Mapnik") {

                private static final long serialVersionUID = -7264367289609795290L;

                @Override
                public String getUrl() {
                    return String.format("http://www.openstreetmap.org/?lat=%f&lon=%f&zoom=%d&layers=M", mapWindow.getCenterLat(), mapWindow.getCenterLon(), mapWindow.getZoom());
                }
            };
            JButton buttonPotlatch1 = new UrlButton("Potlatch 1") {

                private static final long serialVersionUID = -5705984576501268795L;

                @Override
                public String getUrl() {
                    return String.format("http://www.openstreetmap.org/edit?editor=potlatch&lat=%f&lon=%f&zoom=%d&layers=M", mapWindow.getCenterLat(), mapWindow.getCenterLon(), mapWindow.getZoom());
                }
            };
            JButton buttonPotlatch2 = new UrlButton("Potlatch 2") {

                private static final long serialVersionUID = 2759613679591189523L;

                @Override
                public String getUrl() {
                    return String.format("http://www.openstreetmap.org/edit?editor=potlatch2&lat=%f&lon=%f&zoom=%d&layers=M", mapWindow.getCenterLat(), mapWindow.getCenterLon(), mapWindow.getZoom());
                }
            };
            JButton buttonCopyDouble = new ClipboardButton("Clipboard (floating point)") {

                private static final long serialVersionUID = -139734902428928357L;

                @Override
                public String getClipboardText() {
                    String text = mapWindow.getCenterLat() + "," + mapWindow.getCenterLon();
                    return text;
                }
            };
            JButton buttonCopyDegMinSec = new ClipboardButton("Clipboard (deg/min/sec/)") {

                private static final long serialVersionUID = 9182954207805942607L;

                @Override
                public String getClipboardText() {
                    String text = degMinSec(mapWindow.getCenterLat(), mapWindow.getCenterLon());
                    return text;
                }

                private String degMinSec(double centerLat, double centerLon) {
                    String text = degMinSecLat(centerLat) + "," + degMinSecLon(centerLon);
                    return text;
                }

                private String degMinSecLat(double lat) {
                    String letter = lat >= 0 ? "N" : "S";
                    double abs = Math.abs(lat);
                    return degs(abs) + "/" + mins(abs) + "/" + secs(abs) + "/" + letter;
                }

                private String degMinSecLon(double lon) {
                    String letter = lon >= 0 ? "E" : "W";
                    double abs = Math.abs(lon);
                    return degs(abs) + "/" + mins(abs) + "/" + secs(abs) + "/" + letter;
                }

                private int degs(double d) {
                    return (int) Math.floor(d);
                }

                private int mins(double d) {
                    return ((int) Math.round((d - degs(d)) * 3600)) / 60;
                }

                private int secs(double d) {
                    return ((int) Math.round((d - degs(d)) * 3600)) % 60;
                }
            };
            buttons.add(buttonCopyDouble);
            buttons.add(buttonCopyDegMinSec);
            buttons.add(buttonOsmWeb);
            buttons.add(buttonPotlatch1);
            buttons.add(buttonPotlatch2);
            c.weightx = 1.0;
            c.weighty = 0.0;
            c.anchor = GridBagConstraints.PAGE_START;
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 2;
            c.fill = GridBagConstraints.HORIZONTAL;
            add(label, c);
            c.gridwidth = 1;
            c.gridy = 1;
            c.gridx = 0;
            add(labelLon, c);
            c.gridx = 1;
            add(labelCenterLon, c);
            c.gridy = 2;
            c.gridx = 0;
            add(labelLat, c);
            c.gridx = 1;
            add(labelCenterLat, c);
            c.gridx = 0;
            c.gridwidth = 2;
            for (JButton button : buttons) {
                c.gridy += 1;
                add(button, c);
            }
            c.weighty = 1.0;
            add(new JPanel(), c);
        }

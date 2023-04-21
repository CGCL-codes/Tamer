    public void ReadMSFile(String _inputfile, Vector _Spectra) {
        int i;
        int _spot_index;
        String _job_run_id;
        String _job_id;
        String _spot_id;
        String _job_item_id;
        String _precursor_mass;
        String _charge;
        String _peak_list_id;
        String _centroid;
        String _peak_height;
        Vector _aux_values;
        Vector _aux_mass;
        Vector _aux_intensity;
        int _line_count;
        int tabPos, nexttabPos;
        try {
            URL url = new URL(_inputfile);
            BufferedReader inStream = new BufferedReader(new InputStreamReader(url.openStream()));
            _aux_values = new Vector();
            _aux_mass = new Vector();
            _aux_intensity = new Vector();
            _line_count = 0;
            _spot_index = 0;
            _spot_id = Integer.toString(_spot_index);
            _job_item_id = "";
            _peak_list_id = "";
            _precursor_mass = "";
            _charge = "";
            do {
                String inLine;
                if ((inLine = inStream.readLine()) == null) break;
                inLine = inLine.trim();
                _line_count++;
                _aux_values.removeAllElements();
                if (!inLine.equals("")) {
                    if (inLine.compareTo(BEGINIONS) == 0) {
                        _precursor_mass = "";
                        _charge = "";
                        _aux_mass.clear();
                        _aux_intensity.clear();
                        _spot_id = Integer.toString(_spot_index);
                        _job_item_id = "";
                        _peak_list_id = "";
                        _spot_index++;
                    } else {
                        if (inLine.compareTo(ENDIONS) == 0) {
                            CreateSpectrum(_spot_id, _job_item_id, _peak_list_id, _precursor_mass, _charge);
                            MySpectrum.FillMassValues(_aux_mass, _aux_intensity);
                            _Spectra.addElement((TSpectrum) MySpectrum);
                        } else {
                            if (inLine.startsWith(PEPMASS)) _precursor_mass = ParsePrecursorMass(inLine); else if (inLine.startsWith(CHARGE)) _charge = ParseCharge(inLine); else if (inLine.startsWith(TITLE)) _peak_list_id = ParseTitle(inLine); else if ((tabPos = inLine.indexOf(ARRAYS_SEP)) > 0) {
                                _centroid = inLine.substring(0, tabPos);
                                if ((nexttabPos = inLine.indexOf(ARRAYS_SEP, tabPos + 1)) <= 0) nexttabPos = inLine.length();
                                _peak_height = inLine.substring(tabPos + 1, nexttabPos);
                                _aux_mass.addElement((String) _centroid);
                                _aux_intensity.addElement((String) _peak_height);
                            } else if ((tabPos = inLine.indexOf(ARRAYS_TAB)) > 0) {
                                _centroid = inLine.substring(0, tabPos);
                                if ((nexttabPos = inLine.indexOf(ARRAYS_TAB, tabPos + 1)) <= 0) nexttabPos = inLine.length();
                                _peak_height = inLine.substring(tabPos + 1, nexttabPos);
                                _aux_mass.addElement((String) _centroid);
                                _aux_intensity.addElement((String) _peak_height);
                            }
                        }
                    }
                }
            } while (true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

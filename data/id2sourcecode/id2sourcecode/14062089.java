    private static GeneralPathX parseOracleStruct(STRUCT s) throws SQLException {
        GeneralPathX resp = new GeneralPathX();
        ARRAY infoARRAY = null;
        ARRAY ordsARRAY = null;
        Datum[] info_array = null;
        Datum[] ords_array = null;
        int info_array_size = 0;
        int[] start_ind;
        int[] end_ind;
        int dims = 0;
        boolean next_must_do_first = true;
        Datum[] aux = s.getOracleAttributes();
        infoARRAY = (ARRAY) aux[3];
        ordsARRAY = (ARRAY) aux[4];
        dims = ((NUMBER) aux[0]).intValue() / 1000;
        if (dims == 0) {
            dims = 2;
        }
        info_array = (Datum[]) infoARRAY.getOracleArray();
        ords_array = (Datum[]) ordsARRAY.getOracleArray();
        info_array_size = info_array.length / 3;
        int last_index = ords_array.length - dims + 1;
        start_ind = new int[info_array_size];
        end_ind = new int[info_array_size];
        for (int i = 0; i < info_array_size; i++) start_ind[i] = ((NUMBER) info_array[3 * i]).intValue();
        for (int i = 0; i < (info_array_size - 1); i++) end_ind[i] = start_ind[i + 1] - 1;
        end_ind[info_array_size - 1] = last_index;
        int lineType = PathIterator.SEG_LINETO;
        if (end_ind[0] == 0) {
            for (int i = 1; i < info_array_size; i++) {
                lineType = getLineToType(info_array, i);
                next_must_do_first = addOrdsToGPX(resp, start_ind[i] - 1, end_ind[i] - 1, ords_array, dims, lineType, (i == 1), next_must_do_first);
            }
        } else {
            for (int i = 0; i < info_array_size; i++) {
                lineType = getLineToType(info_array, i);
                addOrdsToGPX(resp, start_ind[i] - 1, end_ind[i] - 1, ords_array, dims, lineType, true, true);
            }
        }
        return resp;
    }

    public static int binaereSuche(int LiIndex, int ReIndex, int element, int[] array) {
        if (LiIndex > ReIndex) return -1;
        if (LiIndex == ReIndex) {
            if (element < array[LiIndex]) {
                return LiIndex;
            } else {
                return LiIndex + 1;
            }
        } else {
            int NeuLi, NeuRe, PivotIndex;
            PivotIndex = (ReIndex + LiIndex) / 2;
            if (element < array[PivotIndex]) {
                NeuLi = LiIndex;
                NeuRe = PivotIndex;
                return binaereSuche(NeuLi, NeuRe, element, array);
            } else {
                NeuLi = PivotIndex + 1;
                NeuRe = ReIndex;
                return binaereSuche(NeuLi, NeuRe, element, array);
            }
        }
    }

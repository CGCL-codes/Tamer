    private void quickSort(Vector elements, int lowIndex, int highIndex) {
        int lowToHighIndex = lowIndex;
        int highToLowIndex = highIndex;
        int pivotIndex = (lowToHighIndex + highToLowIndex) / 2;
        double pivotValue = ((TSpectrum) elements.elementAt(pivotIndex)).GetPeptideMass();
        int newLowIndex = highIndex + 1;
        int newHighIndex = lowIndex - 1;
        do {
            if (newHighIndex + 1 >= newLowIndex) break;
            double lowToHighValue;
            for (lowToHighValue = ((TSpectrum) elements.elementAt(lowToHighIndex)).GetPeptideMass(); (lowToHighIndex < newLowIndex) & (lowToHighValue - pivotValue < 0); lowToHighValue = ((TSpectrum) elements.elementAt(lowToHighIndex)).GetPeptideMass()) {
                newHighIndex = lowToHighIndex;
                lowToHighIndex++;
            }
            double highToLowValue;
            for (highToLowValue = ((TSpectrum) elements.elementAt(highToLowIndex)).GetPeptideMass(); (newHighIndex <= highToLowIndex) & (highToLowValue - pivotValue > 0); highToLowValue = ((TSpectrum) elements.elementAt(highToLowIndex)).GetPeptideMass()) {
                newLowIndex = highToLowIndex;
                highToLowIndex--;
            }
            if (lowToHighIndex == highToLowIndex) newHighIndex = lowToHighIndex; else if (lowToHighIndex < highToLowIndex) {
                double compareResult = lowToHighValue - highToLowValue;
                if (compareResult >= 0) {
                    TSpectrum parking = (TSpectrum) elements.elementAt(lowToHighIndex);
                    elements.setElementAt(((TSpectrum) elements.elementAt(highToLowIndex)), lowToHighIndex);
                    elements.setElementAt(parking, highToLowIndex);
                    newLowIndex = highToLowIndex;
                    newHighIndex = lowToHighIndex;
                    lowToHighIndex++;
                    highToLowIndex--;
                }
            }
        } while (true);
        if (lowIndex < newHighIndex) quickSort(elements, lowIndex, newHighIndex);
        if (newLowIndex < highIndex) quickSort(elements, newLowIndex, highIndex);
    }

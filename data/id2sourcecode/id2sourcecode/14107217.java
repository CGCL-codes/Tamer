        private void fromModelToLabels() {
            DensityChange dc = ((DensityChange) pgm.getInnerCircle().getNext());
            int numberOfRpeats = pgm.getNumberOfRepeats();
            double densityCircumference = dc.getCircumference();
            double innerCircumeference = pgm.getInnerCircle().getCircumference();
            double outerCircumference = pgm.getOuterCircle().getCircumference();
            double averageCircumference = (innerCircumeference + outerCircumference) / 2;
            lblCircumInside.setText(roundString(innerCircumeference));
            lblCircumOutside.setText(roundString(outerCircumference));
            lblCircumDensityChange.setText(roundString(densityCircumference));
            int numberOfDots = pgm.getDotsPerRepeat() * numberOfRpeats;
            lblDistanceInside.setText(roundString(innerCircumeference / numberOfDots));
            lblDistanceInsideStraight.setText(roundString(averageCircumference / numberOfDots));
            lblDistanceOldDensityStraight.setText(roundString(averageCircumference / numberOfDots));
            lblDistanceOldDensity.setText(roundString(densityCircumference / numberOfDots));
            numberOfDots = dc.getDotsPerRepeat() * numberOfRpeats;
            lblDistanceOutside.setText(roundString(outerCircumference / numberOfDots));
            lblDistanceOutsideStraight.setText(roundString(averageCircumference / numberOfDots));
            lblDistanceNewDensityStraight.setText(roundString(averageCircumference / numberOfDots));
            lblDistanceNewDensity.setText(roundString(densityCircumference / numberOfDots));
            setDependencies();
        }

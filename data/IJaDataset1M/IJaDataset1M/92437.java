package nu.entities;

import nu.transformations.NUAMDatumUtils;
import java.util.Vector;
import java.lang.Math;

public class NUAMScalarDatum implements NUAMDatum {

    private double magnitude;

    private double error;

    private String annotation;

    private Vector reference, normalized;

    private boolean isNormalized;

    private NUAMScalarDatum normDatum;

    private NUAMDatumUtils utils;

    public NUAMScalarDatum() {
        magnitude = 0;
        error = 0;
        annotation = " ";
        reference = null;
        normalized = null;
        isNormalized = false;
        normDatum = null;
        utils = new NUAMDatumUtils();
    }

    public NUAMScalarDatum(double mg, double er, String an) {
        magnitude = mg;
        error = er;
        annotation = an;
        reference = null;
        normalized = null;
        isNormalized = false;
        normDatum = null;
        utils = new NUAMDatumUtils();
    }

    public double getMagnitude() {
        return magnitude;
    }

    public double getError() {
        return error;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setMagnitude(double mg) {
        magnitude = mg;
    }

    public void setError(double er) {
        error = er;
    }

    public void setAnnotation(String an) {
        annotation = an;
    }

    public void setVector(Vector r, Vector n) {
        reference = r;
        normalized = n;
    }

    @SuppressWarnings("unchecked")
    private void normalize() {
        if (isNormalized) return;
        double xmax = utils.findScalarXmax(reference);
        double xmin = utils.findScalarXmin(reference);
        double emax = utils.findScalarEmax(reference);
        double emin = utils.findScalarEmin(reference);
        double nmagnitude = (magnitude - (xmax - xmin) / 2) / (xmax - xmin + 1);
        double nerror = (error - (emax - emin) / 2) / (emax - emin + 1);
        NUAMScalarDatum ndatum = new NUAMScalarDatum(nmagnitude, nerror, this.annotation);
        ndatum.isNormalized = true;
        normalized.add((Object) ndatum);
        normDatum = ndatum;
        isNormalized = true;
    }

    public double weightedObservation() {
        if (!isNormalized) normalize();
        double xmap = normDatum.magnitude / (1 + Math.abs(normDatum.magnitude));
        double emap = normDatum.error / (1 + Math.abs(normDatum.error));
        return xmap * Math.exp(emap);
    }
}

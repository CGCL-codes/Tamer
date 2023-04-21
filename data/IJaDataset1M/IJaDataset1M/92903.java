package org.opencarto.applet;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.opencarto.applet.xml.BasicStyleType;
import org.opencarto.applet.xml.ColorType;
import org.opencarto.applet.xml.GMapType;
import org.opencarto.applet.xml.HeatStyleType;
import org.opencarto.applet.xml.ImageSourceType;
import org.opencarto.applet.xml.LayerType;
import org.opencarto.applet.xml.OGCWMSType;
import org.opencarto.applet.xml.Opencarto;
import org.opencarto.applet.xml.OsmType;
import org.opencarto.applet.xml.VecType;
import org.opencarto.applet.xml.WithURLSourceType;
import org.opencarto.applet.xml.YmapType;
import org.opencarto.gps.gpx.GPXFileDataSource;
import org.opencarto.gps.tcx.TCXFileDataSource;
import org.opencarto.image.ImageFileDataSource;
import org.opencarto.kml.KMLFileDataSource;
import org.opencarto.kmz.KMZFileDataSource;
import org.opencarto.style.BasicStyle;
import org.opencarto.style.HeatStyle;
import org.opencarto.util.ProjectionUtil;
import org.opencarto.vectortile.VecDataSource;
import org.opencarto.viewer.Layer;
import org.opencarto.viewer.Viewer;
import org.opencarto.wms.ex.GMap;
import org.opencarto.wms.ex.OGC;
import org.opencarto.wms.ex.OSM;
import org.opencarto.wms.ex.YMap;

/**
 * 
 * Required settings for loading.
 * 
 * @author julien Gaffuri
 *
 */
public class Settings implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(Settings.class.getName());

    public Settings() {
    }

    /**
	 * Load settings
	 * 
	 * @param v
	 * @param settingsURL
	 */
    public static void load(Viewer v, URL settingsURL) {
        InputStream is;
        try {
            if ("file".equals(settingsURL.getProtocol())) is = new FileInputStream(settingsURL.getFile()); else if ("http".equals(settingsURL.getProtocol())) is = settingsURL.openStream(); else {
                logger.warning("Impossible to load settings from " + settingsURL + ". Unsupported protocol " + settingsURL.getProtocol());
                return;
            }
        } catch (IOException e) {
            logger.warning("Impossible to load settings from " + settingsURL.getPath());
            return;
        }
        Opencarto oc = null;
        try {
            Unmarshaller u = JAXBContext.newInstance(Opencarto.class.getPackage().getName()).createUnmarshaller();
            oc = (Opencarto) u.unmarshal(is);
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (JAXBException e) {
            logger.warning("Impossible to load settings from " + settingsURL + ". Unmarshalling problem.");
            e.printStackTrace();
            return;
        }
        if (oc == null) {
            logger.warning("Impossible to load settings from " + settingsURL + ". Unmarshalling problem.");
            return;
        }
        if (oc.getMaxzoom() != null) v.setMaxZoom(oc.getMaxzoom().intValue());
        if (oc.getMinzoom() != null) v.setMinZoom(oc.getMinzoom().intValue());
        if (oc.getZoom() != null) v.setZoomLevel(oc.getZoom().intValue());
        if (oc.getLon() != null) v.getGeoCenter().x = ProjectionUtil.getXGeo(oc.getLon().doubleValue());
        if (oc.getLat() != null) v.getGeoCenter().y = ProjectionUtil.getYGeo(oc.getLat().doubleValue());
        if (oc.isGeneralisation() != null) v.setGeneralisation(oc.isGeneralisation().booleanValue());
        if (oc.isScaleBar() != null) v.setScaleBarDisplay(oc.isScaleBar().booleanValue());
        if (oc.isCursorPosition() != null) v.setCursorPositionDisplay(oc.isCursorPosition().booleanValue());
        if (oc.getSelection() != null) v.setSelectionType(oc.getSelection().intValue());
        if (oc.getPopup() != null) v.setPopupDisplayType(oc.getPopup().intValue());
        if (oc.getMagnifier() != null) {
            v.setMagnifier(oc.getMagnifier().doubleValue());
        }
        for (LayerType layt : oc.getLayer()) {
            Layer lay = new Layer(v);
            if (layt.getName() != null) lay.setLayerName(layt.getName());
            if (layt.isVisible() != null) lay.setVisible(layt.isVisible().booleanValue());
            if (layt.isClickable() != null) lay.setClickable(layt.isClickable().booleanValue());
            if (layt.isModifiable() != null) lay.setModifiable(layt.isModifiable().booleanValue());
            for (GMapType source : layt.getSources().getGmap()) {
                GMap ds = new GMap();
                if (source.getURL() != null) ds.setUrl(source.getURL());
                if (source.getLayerURLPart() != null) ds.setLayerURLPart(source.getLayerURLPart());
                ds.setLayer(lay);
                lay.getDataSources().add(ds);
            }
            for (WithURLSourceType source : layt.getSources().getGpx()) {
                GPXFileDataSource ds = new GPXFileDataSource();
                ds.setUrl(source.getURL());
                ds.setLayer(lay);
                lay.getDataSources().add(ds);
            }
            for (ImageSourceType source : layt.getSources().getImage()) {
                ImageFileDataSource ds = new ImageFileDataSource();
                ds.setUrl(source.getURL());
                ds.setLonMin(source.getLonMin());
                ds.setLonMax(source.getLonMax());
                ds.setLatMin(source.getLatMin());
                ds.setLatMax(source.getLatMax());
                ds.setLayer(lay);
                lay.getDataSources().add(ds);
            }
            for (WithURLSourceType source : layt.getSources().getKml()) {
                KMLFileDataSource ds = new KMLFileDataSource();
                ds.setUrl(source.getURL());
                ds.setLayer(lay);
                lay.getDataSources().add(ds);
            }
            for (WithURLSourceType source : layt.getSources().getKmz()) {
                KMZFileDataSource ds = new KMZFileDataSource();
                ds.setUrl(source.getURL());
                ds.setLayer(lay);
                lay.getDataSources().add(ds);
            }
            for (WithURLSourceType source : layt.getSources().getTcx()) {
                TCXFileDataSource ds = new TCXFileDataSource();
                ds.setUrl(source.getURL());
                ds.setLayer(lay);
                lay.getDataSources().add(ds);
            }
            for (OGCWMSType source : layt.getSources().getOgcwms()) {
                OGC ds = new OGC();
                ds.setUrl(source.getURL());
                if (source.getVersion() != null) ds.setVersion(source.getVersion());
                if (source.getLayers() != null) ds.setLayers(source.getLayers());
                if (source.getStyles() != null) ds.setStyles(source.getStyles());
                if (source.getFormat() != null) ds.setFormat(source.getFormat());
                ds.setLayer(lay);
                lay.getDataSources().add(ds);
            }
            for (OsmType source : layt.getSources().getOsm()) {
                OSM ds = new OSM();
                if (source.getURL() != null) ds.setUrl(source.getURL());
                ds.setLayer(lay);
                lay.getDataSources().add(ds);
            }
            for (YmapType source : layt.getSources().getYmap()) {
                YMap ds = new YMap();
                if (source.getURL() != null) ds.setUrl(source.getURL());
                ds.setLayer(lay);
                lay.getDataSources().add(ds);
            }
            for (VecType source : layt.getSources().getVec()) {
                VecDataSource ds = new VecDataSource();
                ds.setUrl(source.getURL());
                ds.setLayer2(source.getLayer());
                ds.setLayer(lay);
                lay.getDataSources().add(ds);
            }
            if (layt.getBasicStyle() != null) {
                BasicStyleType sty = layt.getBasicStyle();
                BasicStyle st = new BasicStyle();
                if (sty.getBorderColor() != null) {
                    ColorType col = sty.getBorderColor();
                    Color col_ = st.getBorderColor();
                    if (col.getRed() != null) col_ = new Color(col.getRed().intValue(), col_.getGreen(), col_.getBlue(), col_.getAlpha());
                    if (col.getGreen() != null) col_ = new Color(col_.getRed(), col.getGreen().intValue(), col_.getBlue(), col_.getAlpha());
                    if (col.getBlue() != null) col_ = new Color(col_.getRed(), col_.getGreen(), col.getBlue().intValue(), col_.getAlpha());
                    if (col.getAlpha() != null) col_ = new Color(col_.getRed(), col_.getGreen(), col_.getBlue(), col.getAlpha().intValue());
                    st.setBorderColor(col_);
                }
                if (sty.getFillColor() != null) {
                    ColorType col = sty.getFillColor();
                    Color col_ = st.getFillColor();
                    if (col.getRed() != null) col_ = new Color(col.getRed().intValue(), col_.getGreen(), col_.getBlue(), col_.getAlpha());
                    if (col.getGreen() != null) col_ = new Color(col_.getRed(), col.getGreen().intValue(), col_.getBlue(), col_.getAlpha());
                    if (col.getBlue() != null) col_ = new Color(col_.getRed(), col_.getGreen(), col.getBlue().intValue(), col_.getAlpha());
                    if (col.getAlpha() != null) col_ = new Color(col_.getRed(), col_.getGreen(), col_.getBlue(), col.getAlpha().intValue());
                    st.setFillColor(col_);
                }
                if (sty.getBorderWidth() != null) st.setBorderWidth(sty.getBorderWidth().intValue());
                lay.setDefaultStyle(st);
            } else if (layt.getHeatStyle() != null) {
                HeatStyleType sty = layt.getHeatStyle();
                HeatStyle st = new HeatStyle();
                if (sty.getColor() != null) {
                    ColorType col = sty.getColor();
                    Color col_ = st.getColor();
                    if (col.getRed() != null) col_ = new Color(col.getRed().intValue(), col_.getGreen(), col_.getBlue(), col_.getAlpha());
                    if (col.getGreen() != null) col_ = new Color(col_.getRed(), col.getGreen().intValue(), col_.getBlue(), col_.getAlpha());
                    if (col.getBlue() != null) col_ = new Color(col_.getRed(), col_.getGreen(), col.getBlue().intValue(), col_.getAlpha());
                    if (col.getAlpha() != null) col_ = new Color(col_.getRed(), col_.getGreen(), col_.getBlue(), col.getAlpha().intValue());
                    st.setColor(col_);
                }
                if (sty.getRadius() != null) st.setRadius(sty.getRadius().intValue());
                if (sty.getRingNb() != null) st.setRingNb(sty.getRingNb().intValue());
                lay.setDefaultStyle(st);
            }
            v.addLayer(lay);
        }
    }
}

package ch.javablog.mobile.trakkcor.controller;

import ch.javablog.mobile.trakkcor.model.BluetoothDeviceList;
import ch.javablog.mobile.trakkcor.service.GPSReceiver;
import ch.javablog.mobile.trakkcor.model.TrackList;
import ch.javablog.mobile.trakkcor.rms.Configuration;
import javax.microedition.lcdui.Displayable;

/**
 *
 * @author Heiko Maa�
 */
public interface ExecutionContext {

    /**
     * Gets the GPSReceiver object which is responsible for 
     * communicate with an external gps receiver.
     */
    public GPSReceiver getGPSReceiver();

    /**
     * Gets a Displayable object under the given name. Each UI
     * of {@link ch.javablog.mobile.trakkcor.ui} package is mapped
     * to an unique name.
     */
    public Displayable getDisplayable(String uiName);

    /**
     * Gets the central configuration object.
     */
    public Configuration getConfiguration();

    /**
     * Gets the TrackList which is used to load and save tracks.
     */
    public TrackList getTrackList();

    /**
     * Gets the BluetoothDeviceList which stores the retrieved bluetooth
     * devices.
     */
    public BluetoothDeviceList getBluetoothDeviceList();
}

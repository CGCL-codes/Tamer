package org.photovault.swingui.color;

import java.util.List;
import org.photovault.image.PhotovaultImage;
import org.photovault.imginfo.ChangePhotoInfoCommand;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.imginfo.PhotoInfoFields;

/**
 Interface that {@link ColorSettingsDialogController} uses to manage color 
 settings in image viewing component.
 
 */
public interface ColorSettingsPreview {

    /**
     Set the field value in preview
     @param field The field to set
     @param value New value for the field
     @param refValues Reference values to show if any
     */
    public void setField(PhotoInfoFields field, Object value, List refValues);

    /**
     Get the photo currently shown in image viewer
     */
    public PhotoInfo getPhoto();

    /**
     Get the currently displayed image
     
     @return Image currently displayed in the preview control or <code>null</code>
     if none.
     */
    public PhotovaultImage getImage();
}

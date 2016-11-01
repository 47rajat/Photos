package com.wssholmes.stark.photos;

import com.google.api.client.util.Key;
import com.kinvey.java.LinkedResources.LinkedGenericJson;

/**
 * Created by stark on 1/11/16.
 */

public class ImageLinkedEntity extends LinkedGenericJson {

    @Key("text")
    private String text;

    private boolean mUploadStatus = false;

    public ImageLinkedEntity(String key) {
        putFile(key);
    }

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }

    public void changeUploadStatus(Boolean status){
        mUploadStatus = status;
    }

    public Boolean getUploadStatus(){
        return mUploadStatus;
    }
}

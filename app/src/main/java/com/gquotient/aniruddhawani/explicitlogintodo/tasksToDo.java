package com.gquotient.aniruddhawani.explicitlogintodo;

/**
 * Created by Aniruddha Wani on 23-Apr-15.
 */

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.kinvey.java.model.KinveyMetaData;

public class tasksToDo extends GenericJson {

    @Key("_id")
    private String id;
    @Key
    private String text;
    /*
    @Key
    private String status;
    */
    @Key
    private String date;
    @Key("_kmd")
    private KinveyMetaData meta; // Kinvey metadata, OPTIONAL
    @Key("_acl")
    private KinveyMetaData.AccessControlList acl; //Kinvey access control, OPTIONAL

    public tasksToDo() {
    }  //GenericJson classes must have a public empty constructor

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
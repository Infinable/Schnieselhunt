package com.android.highlifestudio.schnieselhunt;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


/**
 * describes one SLocation with its longitude latitude and the riddle infos
 */
public class SLocation implements Parcelable,Serializable{

    private final static long serialVersionUID= 42L;
    public String rätseltext;
    public String picpath;
    //contains longitude latitude
    double longitude;
    double latitude;

    public SLocation(String rätseltext, String picpath, double longitude, double latitude){
        this.rätseltext=rätseltext;
        this.picpath=picpath;
        this.longitude=longitude;
        this.latitude=latitude;

    }
    protected SLocation(Parcel in) {
        rätseltext = in.readString();
        picpath=in.readString();
        longitude = in.readDouble();
        latitude= in.readDouble();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rätseltext);
        dest.writeString(picpath);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SLocation> CREATOR = new Creator<SLocation>() {
        @Override
        public SLocation createFromParcel(Parcel in) {
            return new SLocation(in);
        }

        @Override
        public SLocation[] newArray(int size) {
            return new SLocation[size];
        }
    };
}

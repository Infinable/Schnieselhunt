package com.android.highlifestudio.schnieselhunt;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Schnitzeljagd-Object
 * contains mostly general Infos to the Schnitzeljagd and
 * contains the List of Lcoations in which the riddles and
 * associated pictures are
 */
public class Schnitzeljagd implements Parcelable, Serializable{
    private final static long serialVersionUID= 42L;
    String name, startpunkt, zeit, entfernung, zuletztGespielt,difficulty;
    ArrayList<SLocation> SLocationArrayList;
    Schnitzeljagd(String name, String startpunkt, String zeit, String entfernung, String zuletztGespielt,String difficulty, ArrayList<SLocation> SLocationArrayList){
        this.name=name;
        this.startpunkt = startpunkt;
        this.zeit=zeit;this.entfernung=entfernung;this.zuletztGespielt=zuletztGespielt;
        this.SLocationArrayList = SLocationArrayList;
    }


    public Schnitzeljagd(Parcel in) {
        name=in.readString();
        startpunkt =in.readString();
        zeit=in.readString();
        difficulty=in.readString();
        entfernung=in.readString();
        zuletztGespielt=in.readString();
        SLocationArrayList =in.readArrayList(SLocation.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.startpunkt);
        dest.writeString(this.zeit);
        dest.writeString(this.entfernung);
        dest.writeString(this.zuletztGespielt);
        dest.writeString(this.difficulty);
        dest.writeList(SLocationArrayList);
    }
    public static final Parcelable.Creator<Schnitzeljagd>CREATOR= new Parcelable.Creator<Schnitzeljagd>(){
        public Schnitzeljagd createFromParcel(Parcel in){
            return new Schnitzeljagd(in);
        }
        public Schnitzeljagd[] newArray(int size){
            return new Schnitzeljagd[size];
        }
    };

    public ArrayList<SLocation> getSLocationArrayList(){
        return SLocationArrayList;
    }
    public String getName() {
        return name;
    }

    public String getStartpunkt() {
        return startpunkt;
    }

    public String getZeit() {
        return zeit;
    }

    public String getEntfernung() {
        return entfernung;
    }

    public String getZuletztGespielt() {
        return zuletztGespielt;
    }
}

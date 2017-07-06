package com.android.highlifestudio.schnieselhunt;

import android.os.Parcel;
import android.os.Parcelable;

public class Schnitzeljagd implements Parcelable{
    String name, startpunkt, zeit, entfernung, zuletztGespielt;
    Schnitzeljagd(String name, String startpunkt, String zeit, String entfernung, String zuletztGespielt){
        this.name=name;
        this.startpunkt = startpunkt;
        this.zeit=zeit;this.entfernung=entfernung;this.zuletztGespielt=zuletztGespielt;
    }

    public Schnitzeljagd(Parcel in) {
        name=in.readString();
        startpunkt =in.readString();
        zeit=in.readString();
        entfernung=in.readString();
        zuletztGespielt=in.readString();
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
    }
    public static final Parcelable.Creator<Schnitzeljagd>CREATOR= new Parcelable.Creator<Schnitzeljagd>(){
        public Schnitzeljagd createFromParcel(Parcel in){
            return new Schnitzeljagd(in);
        }
        public Schnitzeljagd[] newArray(int size){
            return new Schnitzeljagd[size];
        }
    };

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

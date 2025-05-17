package com.svalero.musicandroid.domain;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class Artist implements Parcelable {

    private long id;
    private String name;
    private String registrationDate;
    private boolean isSolist;
    private String image;
    private String country;
    private List<String> albums;

    public Artist(long id, String name, String registrationDate, boolean isSolist, String image, String country, List<String> albums) {
        this.id = id;
        this.name = name;
        this.registrationDate = registrationDate;
        this.isSolist = isSolist;
        this.image = image;
        this.country = country;
        this.albums = albums;
    }

    protected Artist(Parcel in) {
        id = in.readLong();
        name = in.readString();
        country = in.readString();
    }

    public Artist(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };


    public long getId() {return id;}
    public void setId(long id) {this.id = id; }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getRegistrationDate() {return registrationDate;}
    public void setRegistrationDate(String registrationDate) {this.registrationDate = registrationDate;}

    public boolean isSolist() {return isSolist;}
    public void setIsSolist(boolean isSolist) {this.isSolist = isSolist;}

    public String getImage() {return image;}
    public void setImage(String image) {this.image = image;}

    public String getCountry() {return country;}
    public void setCountry(String Country) {this.country = country;}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(country);
    }
}

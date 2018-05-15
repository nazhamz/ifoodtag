package com.nazri.restaurantfoodrecognizer.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Predictions implements Parcelable {

    @SerializedName("TagId")
    @Expose
    private String tagId;
    @SerializedName("Tag")
    @Expose
    private String tag;
    @SerializedName("Probability")
    @Expose
    private double probability;

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tagId);
        dest.writeString(this.tag);
        dest.writeDouble(this.probability);
    }

    public Predictions() {
    }

    protected Predictions(Parcel in) {
        this.tagId = in.readString();
        this.tag = in.readString();
        this.probability = in.readDouble();
    }

    public static final Parcelable.Creator<Predictions> CREATOR = new Parcelable.Creator<Predictions>() {
        @Override
        public Predictions createFromParcel(Parcel source) {
            return new Predictions(source);
        }

        @Override
        public Predictions[] newArray(int size) {
            return new Predictions[size];
        }
    };
}
package com.nazri.restaurantfoodrecognizer.models;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IrisData implements Parcelable {

    @SerializedName("Id")
    @Expose
    private String id;

    @SerializedName("Project")
    @Expose
    private String project;

    @SerializedName("Iteration")
    @Expose
    private String iteration;

    @SerializedName("Created")
    @Expose
    private String created;


    @SerializedName("Predictions")
    @Expose
    private List<Predictions> predictions = null;

    public String getId() {
        return id;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getIteration() {
        return iteration;
    }

    public void setIteration(String iteration) {
        this.iteration = iteration;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public List<Predictions> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<Predictions> prediction) {
        this.predictions = prediction;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.project);
        dest.writeString(this.iteration);
        dest.writeString(this.created);
        dest.writeTypedList(this.predictions);
    }

    public IrisData() {
    }

    protected IrisData(Parcel in) {
        this.project = in.readString();
        this.iteration = in.readString();
        this.created = in.readString();
        this.predictions = in.createTypedArrayList(Predictions.CREATOR);
    }

    public static final Parcelable.Creator<IrisData> CREATOR = new Parcelable.Creator<IrisData>() {
        @Override
        public IrisData createFromParcel(Parcel source) {
            return new IrisData(source);
        }

        @Override
        public IrisData[] newArray(int size) {
            return new IrisData[size];
        }
    };
}
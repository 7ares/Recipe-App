package com.example.lenovo.recipes.recipesDetail;

import android.os.Parcel;
import android.os.Parcelable;

public class StepsDetail implements Parcelable {
    private int id;
    private String shortDescription,
            description,
            videoURL,
            thumbnailURL;

    protected StepsDetail(Parcel in) {
        id = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }

    public static final Creator<StepsDetail> CREATOR = new Creator<StepsDetail>() {
        @Override
        public StepsDetail createFromParcel(Parcel in) {
            return new StepsDetail(in);
        }

        @Override
        public StepsDetail[] newArray(int size) {
            return new StepsDetail[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoURL() {
        return videoURL;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
    }
}

package com.example.bybocam.Model;

import android.net.Uri;

import java.net.URI;

import retrofit2.http.Url;

public class ViewThumbnails {
  Uri uri;

    public ViewThumbnails(Uri uri) {
        this.uri = uri;
    }

    public ViewThumbnails() {
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}

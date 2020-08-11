package com.example.bybocam.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LikedModel {

@SerializedName("code")
@Expose
private String code;
@SerializedName("status")
@Expose
private String status;
@SerializedName("postData")
@Expose
private List<LikedPostData> postData = null;

public String getCode() {
return code;
}

public void setCode(String code) {
this.code = code;
}

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public List<LikedPostData> getPostData() {
return postData;
}

public void setPostData(List<LikedPostData> postData) {
this.postData = postData;
}

}
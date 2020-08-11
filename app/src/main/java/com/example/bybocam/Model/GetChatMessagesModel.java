package com.example.bybocam.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetChatMessagesModel {

@SerializedName("code")
@Expose
private String code;
@SerializedName("status")
@Expose
private String status;
@SerializedName("data")
@Expose
private List<Datum> data = null;

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

public List<Datum> getData() {
return data;
}

public void setData(List<Datum> data) {
this.data = data;
}

    public class Datum {

        @SerializedName("userCommentsId")
        @Expose
        private String userCommentsId;
        @SerializedName("senderId")
        @Expose
        private String senderId;
        @SerializedName("recevierId")
        @Expose
        private String recevierId;
        @SerializedName("messageType")
        @Expose
        private String messageType;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("messageFile")
        @Expose
        private String messageFile;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;

        public String getUserCommentsId() {
            return userCommentsId;
        }

        public void setUserCommentsId(String userCommentsId) {
            this.userCommentsId = userCommentsId;
        }

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public String getRecevierId() {
            return recevierId;
        }

        public void setRecevierId(String recevierId) {
            this.recevierId = recevierId;
        }

        public String getMessageType() {
            return messageType;
        }

        public void setMessageType(String messageType) {
            this.messageType = messageType;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessageFile() {
            return messageFile;
        }

        public void setMessageFile(String messageFile) {
            this.messageFile = messageFile;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

    }

}
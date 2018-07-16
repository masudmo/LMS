package com.example.mariyamasud.maps.meeting;

import android.widget.CheckBox;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by mariyamasud on 13.02.18.
 */

public class Meeting {
        private String meetingId;
        private String meetingName;
        private String meetingRoom;
        private String meetingNotes;
        private String meetingDate;
        private String meetingTime;
        private String userId;

    public Meeting() {
            //this constructor is required
        }

        public Meeting(String meetingId, String meetingName, String meetingRoom, String meetingNotes, String meetingDate,String meetingTime,String userIdRight) {
            this.meetingId = meetingId;
            this.meetingName = meetingName;
            this.meetingRoom = meetingRoom;
            this.meetingNotes = meetingNotes;
            this.meetingDate = meetingDate;
            this.meetingTime = meetingTime;
            this.userId = userIdRight;
        }

        public String getMeetingId() {
            return meetingId;
        }

        public String getMeetingName() {
            return meetingName;
        }


        public String getMeetingRoom() {
            return meetingRoom;
        }

        public String getMeetingNotes() {
            return meetingNotes;
        }

        public String getMeetingDate() {
            return meetingDate;
        }

         public String getMeetingTime() {
        return meetingTime;
    }
         public  String getUserId(){return userId;}
    }


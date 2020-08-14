package com.example.yaneodoo.Info;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class BistroListVO {
    private ArrayList<Bistro> list;

    public class Bistro {
        private Drawable represent_photo;
        private String name, location, tel;

        public Bistro(Drawable represent_photo, String name, String location, String tel) {
            this.represent_photo = represent_photo;
            this.name = name;
            this.location = location;
            this.tel = tel;
        }

        public Drawable getRepresent_photo() {
            return represent_photo;
        }

        public void setRepresent_photo(Drawable represent_photo) {
            this.represent_photo = represent_photo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }
    }

}

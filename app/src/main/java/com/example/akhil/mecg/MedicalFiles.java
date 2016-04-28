package com.example.akhil.mecg;

import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * Created by Akhil on 23-03-2016.
 */
public class MedicalFiles {

        private String name;
        private String centre;
        private ParseObject obj;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCentre() {
            return centre;
        }

        public void setCentre(String centre) {
            this.centre = centre;
        }

        public ParseObject getObj() {
        return obj;
    }

        public void setObj(ParseObject obj) {
        this.obj = obj;
    }
}

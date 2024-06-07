package com.company.myplatformerfinal;

import java.util.ArrayList;

public class Stage {
    private ArrayList<Platform> platforms;

    public Stage() {
        platforms = new ArrayList<>();
    }

    // Gets the list of platforms currently in the stage.
    public ArrayList<Platform> getPlatforms() {
        return platforms;
    }

     //Adds a platform to the current stage.
    public void addPlatform(Platform platform) {
        platforms.add(platform);
    }


}
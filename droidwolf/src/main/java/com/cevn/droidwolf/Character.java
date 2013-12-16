package com.cevn.droidwolf;

import com.google.gson.annotations.Expose;

/**
 * Created by sameer on 12/14/13.
 */
public class Character {
    private int id;
    private int user_id;
    private String name;
    public boolean werewolf;
    private boolean dead;

    private double latitude;
    private double longitude;


    public Character(int id, String name, boolean werewolf) {
        this.id = id;
        this.name = name;
        this.werewolf = werewolf;
    }

    public String getName() {
        return this.name;
    }
}

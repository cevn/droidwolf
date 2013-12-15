package com.cevn.droidwolf;

/**
 * Created by sameer on 12/14/13.
 */
public class Character {
    public int id;
    public String name;
    public boolean werewolf;

    public Character() {
        super();
    }

    public Character(int id, String name, boolean werewolf) {
        this.id = id;
        this.name = name;
        this.werewolf = werewolf;
    }

}

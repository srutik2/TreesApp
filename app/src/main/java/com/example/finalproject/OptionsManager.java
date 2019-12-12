package com.example.finalproject;

interface OptionsManager {

    /**Create a new MediaPlayer with background music. */
    void startPlayer();

    /**Release the player and set it to null. */
    void stopPlayer();

    /**@return whether the music is playing. */
    boolean musicPlaying();

    /**toggle what description the target's snippet shows. */
    void toggleExtendedSnippets();

    /**@return whether targets are using extended snippets. */
    boolean hasExtendedSnippets();
}

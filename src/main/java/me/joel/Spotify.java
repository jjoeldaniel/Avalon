package me.joel;

import se.michaelthelin.spotify.SpotifyApi;

public class Spotify {

    SpotifyApi spotifyAApi = new SpotifyApi.Builder()
            .setClientId("3451401ce3b148039cbba35a2c25cd5f")
            .setClientSecret("6531d1fa12f645b581a8bbce029139f1")
            .build();
}

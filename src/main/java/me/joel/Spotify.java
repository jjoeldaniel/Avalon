package me.joel;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class Spotify {

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setAccessToken("BQCUyypgWW5eKhbMdaZL1nadP0cmbjIVVeE-h1gRTFs9yNBY-vBzJBzMmuNEJnOnt0OiOibRJlvL-OIVMXKg9wTjbzsDss35tJN1442gKDYFAYZ62U0GCa-uwmp0wtar1LP-IfpTOe7umJodOw1TypjvatHFzfFOpw5IkT-emkzHHAQ")
            .setClientId("3451401ce3b148039cbba35a2c25cd5f")
            .setClientSecret("6531d1fa12f645b581a8bbce029139f1")
            .build();

    // Search method
    public static String searchSpotify(String query)
    {
        // Tracks
        if (query.contains("/track/"))
        {
            // separate ID from query
            String id = query;
            id = id.replace("https://open.spotify.com/track/", "");
            String[] array = id.split("\\?", 2);
            id = array[0];
            id = id.replace("[", "");
            id = id.replace("]", "");

            // Search request
            final GetTrackRequest getTrackRequest = spotifyApi.getTrack(id)
                    .build();
            try {
                final CompletableFuture<Track> trackFuture = getTrackRequest.executeAsync();

                // Example Only. Never block in production code.
                final Track track = trackFuture.join();

                return track.getName();
            } catch (CompletionException e) {
                System.out.println("Error: " + e.getCause().getMessage());
            } catch (CancellationException e) {
                System.out.println("Async operation cancelled.");
            }
        }
        // Playlists
        else if (query.contains("/playlist/"))
        {
            System.out.println("Spotify playlist");
        }
        // Albums
        else if (query.contains("/album/"))
        {
            System.out.println("Spotify album");
        }
        return "";
    }

}

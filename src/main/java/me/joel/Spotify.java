package me.joel;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest;

import java.util.Arrays;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class Spotify {

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId("3451401ce3b148039cbba35a2c25cd5f")
            .setClientSecret("6531d1fa12f645b581a8bbce029139f1")
            .build();

    private static final ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials()
            .build();

    public static void clientCredentials_Async() {
        try {
            final CompletableFuture<ClientCredentials> clientCredentialsFuture = clientCredentialsRequest.executeAsync();

            final ClientCredentials clientCredentials = clientCredentialsFuture.join();

            // Set access token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());

        } catch (CompletionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
        } catch (CancellationException e) {
            System.out.println("Async operation cancelled.");
        }
    }

    // Search method
    public static String searchSpotify(String query) {
        // Set token
        clientCredentials_Async();

        // Tracks
        if (query.contains("/track/")) {
            // separate ID from query
            String id = query;
            id = id.replace("https://open.spotify.com/track/", "");
            String[] array = id.split("\\?", 2);
            id = array[0];
            id = id.replace("[", "");
            id = id.replace("]", "");

            // Search title request
            final GetTrackRequest getTrackRequest = spotifyApi.getTrack(id)
                    .build();
            try {
                final CompletableFuture<Track> trackFuture = getTrackRequest.executeAsync();

                final Track track = trackFuture.join();

                // Get artist name
                if (Arrays.stream(Arrays.stream(track.getArtists()).toArray()).findFirst().isPresent()) {
                    String artistName = Arrays.stream(Arrays.stream(track.getArtists()).toArray()).findFirst().get().toString();
                    artistName = artistName.replace("ArtistSimplified(name=", "");
                    String[] array2 = artistName.split(",", 2);
                    artistName = array2[0];
                    artistName = artistName.replace(",", "");

                    return track.getName() + " " + artistName;
                }

                return track.getName();

            } catch (CompletionException e) {
                System.out.println("Error: " + e.getCause().getMessage());
            } catch (CancellationException e) {
                System.out.println("Async operation cancelled.");
            }
        }

        /* TODO: Figure out how to do playlists and albums
        Possibly place in different method and return an ArrayList?
         */
        // Playlists
        else if (query.contains("/playlist/")) {
            System.out.println("Spotify playlist");
        }
        // Albums
        else if (query.contains("/album/")) {
            System.out.println("Spotify album");
        }
        return "";
    }

}

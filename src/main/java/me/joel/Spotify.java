package me.joel;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchAlbumsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class Spotify {
    private static String q;

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId("3451401ce3b148039cbba35a2c25cd5f")
            .setClientSecret("6531d1fa12f645b581a8bbce029139f1")
            .build();

    // Track and Album search requests
    private static final SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(q)
            .build();
    private static final SearchAlbumsRequest searchAlbumsRequest = spotifyApi.searchAlbums(q)
            .build();

    public static void searchSpotify(String query)
    {
        q = query;
    }

    public static void searchTracks_Sync() {
        try {
            final Paging<Track> trackPaging = searchTracksRequest.execute();

            System.out.println("Total: " + trackPaging.getTotal());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void searchTracks_Async() {
        try {
            final CompletableFuture<Paging<Track>> pagingFuture = searchTracksRequest.executeAsync();

            // Thread free to do other tasks...

            // Example Only. Never block in production code.
            final Paging<Track> trackPaging = pagingFuture.join();

            System.out.println("Total: " + trackPaging.getTotal());
        } catch (CompletionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
        } catch (CancellationException e) {
            System.out.println("Async operation cancelled.");
        }
    }

    public static void searchAlbums_Async() {
        try {
            final CompletableFuture<Paging<AlbumSimplified>> pagingFuture = searchAlbumsRequest.executeAsync();

            // Thread free to do other tasks...

            // Example Only. Never block in production code.
            final Paging<AlbumSimplified> albumSimplifiedPaging = pagingFuture.join();

            System.out.println("Total: " + albumSimplifiedPaging.getTotal());
        } catch (CompletionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
        } catch (CancellationException e) {
            System.out.println("Async operation cancelled.");
        }
    }


}

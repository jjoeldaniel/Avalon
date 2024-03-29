package me.joel.commands.music.lavaplayer;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.albums.GetAlbumRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistRequest;
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest;

import java.util.Arrays;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * Used for retrieving album/playlist thumbnail images
 */
public class Spotify {

    static final String client_id = System.getenv("SPOTIFY_CLIENT_ID");
    static final String client_secret = System.getenv("SPOTIFY_CLIENT_SECRET");

    /**
     * SpotifyAPI
     * <p>
     * <a href="https://github.com/spotify-web-api-java/spotify-web-api-java#Documentation">See Documentation Here</a>
     * </p>
     */
    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(client_id)
            .setClientSecret(client_secret)
            .build();

    /**
     * Authentication Flow
     * <p>
     * <a href="https://developer.spotify.com/documentation/general/guides/authorization/client-credentials/">See Documentation Here</a>
     * </p>
     */
    private static final ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials()
            .build();

    /**
     * Generates Spotify API Access Token
     * <p>(Required to make requests)</p>
     */
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

    /**
     * Generates search request for individual Tracks, (public) Playlists, and Albums
     * @param query (User input, valid YouTube/Spotify link)
     * @return Thumbnail
     */
    public static String searchSpotify(String query) {

        // Set token
        clientCredentials_Async();

        // Grab ID
        String id = separateID(query);

        // Track Request
        if (query.contains("/track/")) {
            // Search request
            final GetTrackRequest getTrackRequest = spotifyApi.getTrack(id)
                    .build();
            try {
                final CompletableFuture<Track> trackFuture = getTrackRequest.executeAsync();
                final Track track = trackFuture.join();

                return Arrays.stream(track.getAlbum().getImages()).toList().get(0).getUrl();
            } catch (CompletionException e) {
                System.out.println("Error: " + e.getCause().getMessage());
            } catch (CancellationException e) {
                System.out.println("Async operation cancelled.");
            }
        }

        // Playlist
        if (query.contains("/playlist/")) {

            // Search request
            final GetPlaylistRequest getPlaylistRequest = spotifyApi.getPlaylist(id)
                    .build();
            try {
                final CompletableFuture<Playlist> playlistFuture = getPlaylistRequest.executeAsync();
                final Playlist playlist = playlistFuture.join();

                return Arrays.stream(playlist.getImages()).toList().get(0).getUrl();
            } catch (CompletionException e) {
                System.out.println("Error: " + e.getCause().getMessage());
            } catch (CancellationException e) {
                System.out.println("Async operation cancelled.");
            }
        }

        // Album
        else if (query.contains("/album/")) {

            // Search request
            final GetAlbumRequest getAlbumRequest = spotifyApi.getAlbum(id)
                    .build();
            try {
                final CompletableFuture<Album> albumFuture = getAlbumRequest.executeAsync();
                final Album album = albumFuture.join();

                return Arrays.stream(album.getImages()).toList().get(0).getUrl();
            } catch (CompletionException e) {
                System.out.println("Error: " + e.getCause().getMessage());
            } catch (CancellationException e) {
                System.out.println("Async operation cancelled.");
            }
        }

        return null;
    }

    /**
     * Separates Spotify ID
     * @param url Spotify URL
     * @return Spotify ID
     */
    public static String separateID(String url) {

        String id;
        String type = "";

        if (url.contains("/track/"))  type = "track";
        else if (url.contains("/playlist/"))  type = "playlist";
        else if (url.contains("/album/"))  type = "album";

        id = url.replace("https://open.spotify.com/" + type + "/", "");
        String[] array = id.split("\\?", 2);
        id = array[0];
        id = id.replace("[", "");
        id = id.replace("]", "");

        return id;
    }

}

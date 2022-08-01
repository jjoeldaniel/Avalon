package me.joel;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.albums.GetAlbumRequest;
import se.michaelthelin.spotify.requests.data.albums.GetAlbumsTracksRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistsItemsRequest;
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class Spotify {

    private static String albumThumbnail;
    private static String playlistThumbnail;

    /**
     * SpotifyAPI
     * <p>
     * <a href="https://github.com/spotify-web-api-java/spotify-web-api-java#Documentation">See Documentation Here</a>
     * </p>
     */
    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId("3451401ce3b148039cbba35a2c25cd5f")
            .setClientSecret("6531d1fa12f645b581a8bbce029139f1")
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
     * @return Song/Playlist/Album name + Artist name
     */
    public static String searchSpotify(String query) {
        // Set token
        clientCredentials_Async();

        // Grab ID
        String id = separateID(query);

        // Tracks (Returns track name + artist name)
        if (query.contains("/track/")) {

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

            } catch (CompletionException e) {
                System.out.println("Error: " + e.getCause().getMessage());
            } catch (CancellationException e) {
                System.out.println("Async operation cancelled.");
            }
        }
        // Playlists (returns playlist name)
        else if (query.contains("/playlist/")) {

            // Search title request
            final GetPlaylistRequest getPlaylistRequest = spotifyApi.getPlaylist(id)
                    .build();
            try {
                final CompletableFuture<Playlist> playlistFuture = getPlaylistRequest.executeAsync();
                final Playlist playlist = playlistFuture.join();

                playlistThumbnail = Arrays.stream(playlist.getImages()).toList().get(0).getUrl();

                return playlist.getName();
            } catch (CompletionException e) {
                System.out.println("Error: " + e.getCause().getMessage());
            } catch (CancellationException e) {
                System.out.println("Async operation cancelled.");
            }
        }
        // Albums
        else if (query.contains("/album/")) {

            // Search title request
            final GetAlbumRequest getAlbumRequest = spotifyApi.getAlbum(id)
                    .build();
            try {
                final CompletableFuture<Album> albumFuture = getAlbumRequest.executeAsync();
                final Album album = albumFuture.join();

                albumThumbnail = Arrays.stream(album.getImages()).toList().get(0).getUrl();

                return album.getName();
            } catch (CompletionException e) {
                System.out.println("Error: " + e.getCause().getMessage());
            } catch (CancellationException e) {
                System.out.println("Async operation cancelled.");
            }
        }

        return "";
    }

    /**
     * @param query Track, Playlist, Album URLs
     * @return Tracks in (trackName + artistName) format
     */
    public static ArrayList<String> getTracks(String query) {
        ArrayList<String> tracks = new ArrayList<>();

        String id = separateID(query);

        if (query.contains("/playlist/")) {

            // Get Items Request
            final GetPlaylistsItemsRequest getPlaylistsItemsRequest = spotifyApi
                    .getPlaylistsItems(id)
                    .build();
            try {
                final CompletableFuture<Paging<PlaylistTrack>> pagingFuture = getPlaylistsItemsRequest.executeAsync();
                final Paging<PlaylistTrack> playlistTrackPaging = pagingFuture.join();

                // Add tracks to list
                for (int i = 0; i < 99 && i < playlistTrackPaging.getTotal(); i++) {
                    String trackName = (playlistTrackPaging.getItems()[i].getTrack().getName());

                    // Get artist name
                    String artistName = String.valueOf(((Track) playlistTrackPaging.getItems()[i].getTrack()).getArtists()[0]);
                    artistName = artistName.replace("ArtistSimplified(name=", "");
                    String[] array2 = artistName.split(",", 2);
                    artistName = array2[0];
                    artistName = artistName.replace(",", "");

                    String track = trackName + " " + artistName;
                    tracks.add(track);
                }

                return tracks;

            } catch (CompletionException e) {
                System.out.println("Error: " + e.getCause().getMessage());
            } catch (CancellationException e) {
                System.out.println("Async operation cancelled.");
            }
        }
        else if (query.contains("/album/")) {

            // Get Items Request
            final GetAlbumsTracksRequest getAlbumsTracksRequest = spotifyApi.getAlbumsTracks(id)
                    .build();
            try {
                final CompletableFuture<Paging<TrackSimplified>> pagingFuture = getAlbumsTracksRequest.executeAsync();
                final Paging<TrackSimplified> trackSimplifiedPaging = pagingFuture.join();

                // Add tracks to list
                for (int i = 0; i < trackSimplifiedPaging.getTotal(); i++) {
                    String trackName = trackSimplifiedPaging.getItems()[i].getName();

                    // Get artist name
                    String artistName = Arrays.toString(trackSimplifiedPaging.getItems()[i].getArtists());
                    artistName = artistName.replace("ArtistSimplified(name=", "");
                    String[] array2 = artistName.split(",", 2);
                    artistName = array2[0];
                    artistName = artistName.replace(",", "");

                    String track = trackName + " " + artistName;
                    tracks.add(track);
                }

            } catch (CompletionException e) {
                System.out.println("Error: " + e.getCause().getMessage());
            } catch (CancellationException e) {
                System.out.println("Async operation cancelled.");
            }
        }

        //return tracks;
        return tracks;
    }

    /**
     * Album Thumbnail Getter
     * @return Thumbnail URL
     */
    public static String getAlbumThumbnail() {
        return albumThumbnail;
    }

    /**
     * Playlist Thumbnail Getter
     * @return Thumbnail URL
     */
    public static String getPlaylistThumbnail() {
        return playlistThumbnail;
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

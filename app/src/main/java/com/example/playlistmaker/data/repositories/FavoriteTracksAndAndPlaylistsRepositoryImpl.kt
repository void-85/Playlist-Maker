package com.example.playlistmaker.data.repositories

import com.example.playlistmaker.data.db.AppDB
import com.example.playlistmaker.data.db.DBPlaylistEntity
import com.example.playlistmaker.data.db.DBTrackEntity
import com.example.playlistmaker.domain.db.FavoriteTracksAndPlaylistsRepository
import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.domain.entities.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import java.time.ZoneOffset

class FavoriteTracksAndAndPlaylistsRepositoryImpl(
    private val appDB: AppDB
) : FavoriteTracksAndPlaylistsRepository {

    // -- FAV TRACKS ----------------------------------------------------------
    override suspend fun insertFavTrack(track: Track) {
        appDB.getDAO().insertFavTrack(track.convert())
    }

    override suspend fun deleteFavTrack(track: Track) {
        appDB.getDAO().deleteFavTrack(track.convert().trackId)
    }

    override suspend fun isTrackFavorite(track: Track): Boolean {
        return appDB.getDAO().isTrackFavorite(track.convert().trackId) > 0
    }

    override suspend fun getAllFavTracks(): Flow<Track> = flow {
        appDB.getDAO().getAllFavTracks().forEach {
            emit(it.convert())
        }
    }

    override suspend fun getAllFavTracksIds(): Flow<Long> = flow {
        appDB.getDAO().getAllFavTracksIds().forEach {
            emit(it)
        }
    }
    // -- FAV TRACKS ----------------------------------------------------------



    // -- PLAYLISTS -----------------------------------------------------------
    override suspend fun createPlaylist(playlist: Playlist) {
        appDB.getDAO().createPlaylist(playlist.convert())
    }

    override suspend fun getAllPlaylists(): Flow<Playlist> = flow {
        appDB.getDAO().getAllPlaylists().forEach {
            emit(it.convert())
        }
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        appDB.getDAO().deletePlaylist(playlistId)
    }
    // -- PLAYLISTS -----------------------------------------------------------



    // -- CONVERTERS ----------------------------------------------------------
    private fun Playlist.convert(): DBPlaylistEntity {
        return DBPlaylistEntity(

            id = this.id,

            name = this.name,
            description = this.description,

            imageId = this.imageId,

            tracksJson = Gson().toJson(this.tracks),
            amountOfTracks = this.amountOfTracks
        )
    }
    private fun DBPlaylistEntity.convert(): Playlist {
        return Playlist(
            id = this.id,

            name = this.name,
            description = this.description,

            imageId = this.imageId,

            tracks = Gson().fromJson(
                this.tracksJson,
                object : TypeToken<List<Track>>() {}.type
            ),
            amountOfTracks = this.amountOfTracks
        )
    }


    private fun Track.convert(): DBTrackEntity {
        return DBTrackEntity(

            this.trackId,

            this.trackName,
            this.artistName,
            this.trackTime,
            this.artworkUrl100,

            this.collectionName,
            this.releaseDate,
            this.primaryGenreName,
            this.country,

            this.previewUrl,

            LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        )
    }
    private fun DBTrackEntity.convert(): Track {
        return Track(

            this.trackId,

            this.trackName,
            this.artistName,
            this.trackTime,
            this.artworkUrl100,

            this.collectionName,
            this.releaseDate,
            this.primaryGenreName,
            this.country,

            this.previewUrl,

            isFavorite = false
        )
    }
    // -- CONVERTERS ----------------------------------------------------------
}
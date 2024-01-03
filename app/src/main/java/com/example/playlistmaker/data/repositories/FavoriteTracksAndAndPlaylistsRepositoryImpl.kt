package com.example.playlistmaker.data.repositories

import com.example.playlistmaker.data.db.AppDB
import com.example.playlistmaker.data.db.DBPlaylistEntity
import com.example.playlistmaker.data.db.DBFavoriteTrackEntity
import com.example.playlistmaker.data.db.DBTrackInPlaylist
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

    override suspend fun getAllPlaylistsWithoutTracksData(): Flow<Playlist> = flow {
        appDB.getDAO().getAllPlaylists().forEach {
            emit(it.convertWithoutTracksData())
        }
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        appDB.getDAO().deletePlaylistTracks(playlistId)
        appDB.getDAO().deletePlaylist(playlistId)
    }


    override suspend fun getPlaylist(playlistId: Long): Playlist {

        val playlist = appDB.getDAO().getPlaylist(playlistId).convertWithoutTracksData()

        val tracksInPlaylist = ArrayList<Track>()
        appDB.getDAO().getPlaylistTracks(playlistId).forEach {

            tracksInPlaylist.add(
                Gson().fromJson<Track>(
                    it.jsonData,
                    object : TypeToken<Track>() {}.type
                )
            )
        }

        return Playlist(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            imageId = playlist.imageId,

            tracks = tracksInPlaylist,
            amountOfTracks = tracksInPlaylist.size
        )
    }

    override suspend fun updatePlaylistInfo(playlist: Playlist) {
        appDB.getDAO().modifyPlaylistById(
            playlistId = playlist.id,
            name = playlist.name,
            desc = playlist.description,
            image = playlist.imageId
        )
    }

    override suspend fun checkIfTrackIsInPlaylist(trackId: Long, playlistId: Long): Boolean {
        return appDB.getDAO().checkIfTrackIsInPlaylist(trackId, playlistId) > 0
    }

    override suspend fun addTrackToPlaylist(track: Track, playlistId: Long) {
        appDB.getDAO().addTrackToPlaylist(
            DBTrackInPlaylist(
                recordId = 0L,
                trackId = track.trackId,
                playlistId = playlistId,
                jsonData = Gson().toJson(track),
                whenAddedToPlaylist = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
            )
        )
    }

    override suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long) {
        appDB.getDAO().deleteTrackFromPlaylist(trackId, playlistId)
    }
    // -- PLAYLISTS -----------------------------------------------------------


    // -- CONVERTERS ----------------------------------------------------------
    private fun Playlist.convert(): DBPlaylistEntity {
        return DBPlaylistEntity(

            id = this.id,

            name = this.name,
            description = this.description,

            imageId = this.imageId,
        )
    }

    private suspend fun DBPlaylistEntity.convertWithoutTracksData(): Playlist {
        return Playlist(
            id = this.id,

            name = this.name,
            description = this.description,

            imageId = this.imageId,

            tracks = emptyList(),
            amountOfTracks = appDB.getDAO().getTracksAmountInPlaylist(this.id)
        )
    }


    private fun Track.convert(): DBFavoriteTrackEntity {
        return DBFavoriteTrackEntity(

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

    private fun DBFavoriteTrackEntity.convert(): Track {
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
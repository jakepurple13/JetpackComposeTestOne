package com.halilibo.composevideoplayer

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.ui.geometry.Size
import com.programmersbox.gsonutils.fromJson
import com.programmersbox.gsonutils.toJson
import java.io.Serializable

//@Parcelize
data class VideoPlayerUiState(
    val isPlaying: Boolean = true,
    val controlsVisible: Boolean = true,
    val controlsEnabled: Boolean = true,
    val gesturesEnabled: Boolean = true,
    val duration: Long = 1L,
    val currentPosition: Long = 1L,
    val secondaryProgress: Long = 1L,
    val videoSize: Pair<Float, Float> = 1920f to 1080f,
    val draggingProgress: DraggingProgress? = null,
    val playbackState: PlaybackState = PlaybackState.IDLE,
    val quickSeekAction: QuickSeekAction = QuickSeekAction.none()
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().fromJson<Boolean>() ?: true,
        parcel.readString().fromJson<Boolean>() ?: true,
        parcel.readString().fromJson<Boolean>() ?: true,
        parcel.readString().fromJson<Boolean>() ?: true,
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString().fromJson<Pair<Float, Float>>() ?: 1920f to 1080f,
        parcel.readString().fromJson<DraggingProgress>(),
        parcel.readString().fromJson<PlaybackState>() ?: PlaybackState.IDLE,
        parcel.readString().fromJson<QuickSeekAction>() ?: QuickSeekAction.none()
    )

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(isPlaying.toJson())
        dest?.writeString(controlsVisible.toJson())
        dest?.writeString(controlsEnabled.toJson())
        dest?.writeString(gesturesEnabled.toJson())
        dest?.writeLong(duration)
        dest?.writeLong(currentPosition)
        dest?.writeLong(secondaryProgress)
        dest?.writeString(videoSize.toJson())
        dest?.writeString(draggingProgress.toJson())
        dest?.writeString(playbackState.toJson())
        dest?.writeString(quickSeekAction.toJson())
    }

    companion object CREATOR : Parcelable.Creator<VideoPlayerUiState> {
        override fun createFromParcel(parcel: Parcel): VideoPlayerUiState {
            return VideoPlayerUiState(parcel)
        }

        override fun newArray(size: Int): Array<VideoPlayerUiState?> {
            return arrayOfNulls(size)
        }
    }
}
package src.alquerque.control;

public class AlquerqueSettingController {

    private AlquerquePlaylistController playlist;
    private double dernierVolume = 0.5;
    private boolean musiqueActive = true;

    public AlquerqueSettingController() {
        this.playlist = AlquerquePlaylistController.getInstance();
    }

    public boolean toggleMusique() {
        if (musiqueActive) {
            playlist.setVolume(0);
            musiqueActive = false;
        } else {
            playlist.setVolume(dernierVolume);
            musiqueActive = true;
        }
        return musiqueActive;
    }

    public void setVolume(double v) {
        playlist.setVolume(v);
        musiqueActive = (v > 0);
        if (v > 0) dernierVolume = v;
    }

    public boolean isMusiqueActive() {
        return musiqueActive;
    }

    public double getVolume() {
        return playlist.getVolume();
    }

}
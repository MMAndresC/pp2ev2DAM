package com.svalero.musicrxjava.task;

import com.svalero.musicrxjava.domain.Album;
import com.svalero.musicrxjava.service.MusicService;
import io.reactivex.rxjava3.functions.Consumer;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.util.concurrent.CountDownLatch;

public class GetAlbumsByIdArtist extends Task<Void> {

    private final ObservableList<Album> requestedData;
    private final long idArtist;

    public GetAlbumsByIdArtist(ObservableList<Album> requestedData, long idArtist) {
        this.requestedData = requestedData;
        this.idArtist = idArtist;
    }

    @Override
    protected Void call() throws Exception {
        MusicService musicService = new MusicService();

        // Include it to synchronize Observer with Task, task end before observer
        CountDownLatch latch = new CountDownLatch(1);

        Consumer<Album> consumer = (album -> {
            System.out.println("Data received: " + album);
            Platform.runLater(() -> this.requestedData.add(album));
        });

        musicService.getAlbumsByIdArtist(idArtist).subscribe(
                album -> {
                    System.out.println("Data received: " + album);
                    Platform.runLater(() -> this.requestedData.add(album));
                },
                throwable -> {
                    throwable.printStackTrace();
                    latch.countDown();
                },
                () -> {
                    System.out.println("All albums received");
                    latch.countDown();
                }
        );

        //Stand by until resolve or error
        latch.await();
        return null;
    }
}

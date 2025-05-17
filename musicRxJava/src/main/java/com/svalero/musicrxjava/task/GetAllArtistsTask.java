package com.svalero.musicrxjava.task;

import com.svalero.musicrxjava.domain.Artist;
import com.svalero.musicrxjava.service.MusicService;
import com.svalero.musicrxjava.utils.ErrorLogger;
import io.reactivex.rxjava3.functions.Consumer;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.util.concurrent.CountDownLatch;

public class GetAllArtistsTask extends Task<Void> {

    private final ObservableList<Artist> requestedData;

    public GetAllArtistsTask(ObservableList<Artist> requestedData) {
        this.requestedData = requestedData;
    }

    @Override
    protected Void call() throws Exception {
        MusicService musicService = new MusicService();

        // Include it to synchronize Observer with Task, task end before observer
        CountDownLatch latch = new CountDownLatch(1);

        Consumer<Artist> consumer = (artist -> {
            System.out.println("Data received: " + artist);
            //Space out sending of artists
            Thread.sleep(200);
            Platform.runLater(() -> this.requestedData.add(artist));
        });

        //Subscribe consumer to observable to get items
        musicService.loginAndGetAllArtists().subscribe(consumer, throwable -> {
            System.err.println("Error receiving data: " + throwable.getMessage());
            ErrorLogger.log(throwable.getMessage());
            latch.countDown(); //Error, end task, unlatch
        }, () -> {
            System.out.println("End sending");
            latch.countDown(); // End sending, end task, unlatch
        });

        //Stand by until resolve or error
        latch.await();

        return null;
    }
}

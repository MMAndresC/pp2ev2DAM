module com.svalero.musicrxjava {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires retrofit2;
    requires okhttp3;
    requires com.google.gson;
    requires retrofit2.converter.gson;
    requires retrofit2.adapter.rxjava3;
    requires io.reactivex.rxjava3;
    requires java.logging;

    //MUY IMPORTANTE poner esto o GSON no tendra acceso a las clases de domain y fallara
    opens com.svalero.musicrxjava.domain to com.google.gson;
    opens com.svalero.musicrxjava to javafx.fxml;
    exports com.svalero.musicrxjava;
    exports com.svalero.musicrxjava.controller;
    opens com.svalero.musicrxjava.controller to javafx.fxml;
}
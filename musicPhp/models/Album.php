<?php

namespace models;

class Album {

    public $id;
    //public $name;
    public $title;
    public $releaseDate;
    public $genre;
    public $front;
    public $tracks;
    public $duration;
    public $label;
    public $platinum;


    public function __construct($data) {
        $this->id = $data["id"];
        //$this->name = $data["name"];
        $this->title = $data["title"];
        $this->releaseDate = $data["releaseDate"];
        $this->genre = $data["genre"];
        $this->front = $data["front"];
        $this->tracks = $data["tracks"];
        $this->duration = $data["duration"];
        $this->label = $data["label"];
        $this->platinum = $data["platinum"];

    }

}
?>
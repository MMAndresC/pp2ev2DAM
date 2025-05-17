<?php

namespace models;

class Artist {

    public $id;
    public $name;
    public $country;
    public $registrationDate;
    public $isSoloist;
    public $image;



    public function __construct($data) {
        $this->id = $data["id"];
        $this->name = $data["name"];
        $this->country = $data["country"];
        $this->registrationDate = $data["registrationDate"];
        $this->isSoloist = $data["soloist"];
        $this->image = $data["image"];
    }

}

?>
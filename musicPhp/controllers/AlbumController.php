<?php

use models\Album;
require_once __DIR__ . "/../config.php";
require_once __DIR__ . "/../models/Album.php";




class AlbumController {
    public static function getAllAlbums() {
         $headers = [
            "Authorization: Bearer " . TOKEN,
            "Accept: application/json"
        ];

        $options = [
            "http" => [
                "method" => "GET",
                "header" => implode("\r\n", $headers)
            ]
        ];
        $context = stream_context_create($options);
        $response = file_get_contents(API_URL . "/albums", false, $context);
        $albumsArray = json_decode($response, true);
        $albums = [];

        foreach ($albumsArray as $data) {
            $albums[] = new Album($data);
        }

        return $albums;
    }
}

?>


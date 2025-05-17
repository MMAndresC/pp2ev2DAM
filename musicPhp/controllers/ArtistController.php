<?php

use models\Artist;

require_once __DIR__ . "/../config.php";
require_once __DIR__ . "/../models/Artist.php";


class ArtistController {
    public static function getAllArtist() {
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

        $response = file_get_contents(API_URL . "/artists", false, $context);

        $artistsArray = json_decode($response, true);
        $artist = [];

        foreach ($artistsArray as $data) {
            $artist[] = new Artist($data);
        }

        return $artist;
    }
}
?>


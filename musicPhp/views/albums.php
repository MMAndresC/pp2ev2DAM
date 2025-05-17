<?php
require_once __DIR__ . "/../controllers/AlbumController.php";

$albums = AlbumController::getAllAlbums();

// Manejo de error si la API no responde
if (!is_array($albums)) {
    $albums = [];
}
?>

<div class="container mt-5">
    <h2 class="mb-4">Albums List</h2>

    <?php if (empty($albums)): ?>
        <p class="text-danger">Error loading albums. Test API connection.</p>
    <?php else: ?>
        <table class="table table-striped">
            <thead>
            <tr>
                <th>ID</th>

                <th>Title</th>
                <th>Release Date</th>
                <th>Genre</th>
                <!--<th>Front</th>-->
                <th>Tracks</th>
                <th>Duration</th>
                <th>Label</th>
                <th>Platinum</th>
            </tr>
            </thead>
            <tbody>
            <?php foreach ($albums as $album) { ?>
                <tr>
                    <td><?= htmlspecialchars($album->id) ?></td>

                    <td><?= htmlspecialchars(isset($album->title) ?$album->title : '')  ?></td>
                    <td><?= htmlspecialchars(isset($album->releaseDate) ?$album->releaseDate : '')  ?></td>
                    <td><?= htmlspecialchars(isset($album->genre) ?$album->genre : '')  ?></td>
                    <!--<td><?= htmlspecialchars(isset($album->front) ?$album->front : '')  ?></td>-->
                    <td><?= htmlspecialchars(isset($album->tracks) ?$album->tracks : '')  ?></td>
                    <td><?= htmlspecialchars(isset($album->duration) ?$album->duration : '')  ?></td>
                    <td><?= htmlspecialchars(isset($album->label) ?$album->label : '')  ?></td>
                    <td><?= $album->platinum ? 'Sí' : 'No' ?></td>
                </tr>
            <?php } ?>
            </tbody>
        </table>
    <?php endif; ?>
</div>

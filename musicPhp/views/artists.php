<?php
require_once __DIR__ . "/../controllers/ArtistController.php";

$artists = ArtistController::getAllArtist();

if (!is_array($artists)) {
    $artists = [];
}
?>

<div class="container mt-5">
    <h2 class="mb-4">Artists List</h2>

    <?php if (empty($artists)): ?>
        <p class="text-danger">Error loading artists. Test API connection</p>
    <?php else: ?>
    <table class="table table-striped">
        <thead>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Country</th>
            <th>Registration Date</th>
            <!--<th>Is Soloist</th>-->
            <!--<th>Image</th>-->
        </tr>
        </thead>
        <tbody>
        <?php foreach ($artists as $artist) { ?>
            <tr>
                <td><?= htmlspecialchars(isset($artist->id) ? $artist->id : '') ?></td>
                <td><?= htmlspecialchars(isset($artist->name) ? $artist->name : '') ?></td>
                <td><?= htmlspecialchars(isset($artist->country) ? $artist->country : '')  ?></td>
                <td><?= htmlspecialchars(isset($artist->registrationDate) ? $artist->registrationDate : '')  ?></td>
                <!--<td><?= htmlspecialchars(isset($artist->image) ? $artist->image : '')  ?></td>-->
                <!--<td><?= $artist->isSoloist ? 'Sí' : 'No' ?></td>-->
            </tr>
        <?php } ?>
        </tbody>
    </table>
    <?php endif; ?>
</div>



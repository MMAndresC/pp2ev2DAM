<?php
require_once "controllers/ArtistController.php";
require_once "controllers/AlbumController.php";
?>

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Music App</title>

    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body>
<div class="container text-center mt-5">
    <img class="d-block mx-auto mb-4" src="includes/disco.png" alt="" width="72" height="57">
    <h1 class="display-5 fw-bold">Music APP</h1>
    <br>
    <p class="lead mb-4">The best Music App</p>

    <br>
    <div class="d-grid gap-2 d-sm-flex justify-content-sm-center">
        <a href="index.php?view=artists" class="btn btn-primary btn-lg px-4 gap-3">Artists</a>
        <a href="index.php?view=albums" class="btn btn-outline-secondary btn-lg px-4">Albums</a>
    </div>
</div>

<!-- Sección donde se cargarán las vistas dinámicamente -->
<div class="container mt-5">
    <?php
    if (isset($_GET['view'])) {
        require_once "router.php";
    }
    ?>
</div>

<!-- Bootstrap 5 JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>

</html>

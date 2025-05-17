<?php
/*maneja solicitudes de la pagina , mostrando la vista requerida dependiendo del parametro pasado en la url*/

$request = isset($_GET['view']) ? $_GET['view'] : '';

if ($request == 'artists') {
    require_once "views/artists.php";
} elseif ($request == 'albums') {
    require_once "views/albums.php";
} else {
    echo "<p class='text-center text-danger'>PAGE NOT FOUND!</p>";
}
?>

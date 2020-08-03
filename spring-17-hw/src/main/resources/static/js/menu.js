$(document).ready(function () {
  showMenu();
});

function showMenu() {
  const data = `
        <div>
        <nav class="navbar navbar-inverse">
            <div class="container-fluid">
                <div class="navbar-header">
                    <a class="navbar-brand active" href="/">Library</a>
                </div>
                <ul class="nav navbar-nav">
                    <li><a href="/index">Home</a></li>
                    <li><a href="/books">Books</a></li>
                    <li><a href="/authors">Authors</a></li>
                    <li><a href="/genres">Genres</a></li>
                </ul>
            </div>
        </nav>
    </div>`;
  $("#main_menu").html(data);
}

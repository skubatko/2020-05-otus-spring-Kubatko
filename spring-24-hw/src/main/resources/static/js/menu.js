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
                    <li><a href="/index"><i class="fas fa-home m-2"></i> Home</a></li>
                    <li><a href="/library/books"><i class="fas fa-book-open m-2"></i> Books</a></li>
                </ul>
            </div>
        </nav>
    </div>`;
  $("#main_menu").html(data);
}

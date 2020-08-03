$(document).ready(function () {
  getBooks();
});

const booksTable = $("#booksTable")[0];
const booksApi = "/api/books";

function getBooks() {
  $.get(booksApi, function (books, status, xhr) {
    Object.values(books).forEach((book) => insertBookInList(book, booksTable.rows.length));
  });
}

function createBook() {
  const book = {};
  const createBookName = $("#createBookName");
  const createBookAuthor = $("#createBookAuthor");
  const createBookGenre = $("#createBookGenre");
  const createBookComments = $("#createBookComments");

  book.name = createBookName.val();
  book.author = createBookAuthor.val();
  book.genre = createBookGenre.val();
  book.comments = createBookComments.val();

  $.ajax({
    type: "POST",
    url: booksApi,
    data: JSON.stringify(book),
    contentType: "application/json; charset=utf-8",
    dataType: "json",
    success: function (receivedBook) {
      insertBookInList(receivedBook, booksTable.rows.length);
    },
    error: onError
  });

  createBookName.focus();
  createBookName.val("");
  createBookAuthor.val("");
  createBookGenre.val("");
  createBookComments.val("");
}

function editBook(bookName) {
  let row = booksTable.rows.namedItem(bookName);
  const idx = row.sectionRowIndex;

  const book = {};
  book.name = row.cells[1].innerHTML;
  book.author = row.cells[2].innerHTML;
  book.genre = row.cells[3].innerHTML;
  book.comments = row.cells[4].innerHTML;

  row.remove();
  row = booksTable.insertRow(idx);

  const rowIdAttr = document.createAttribute('id');
  rowIdAttr.value = book.name;
  row.attributes.setNamedItem(rowIdAttr);

  const cellIdx = row.insertCell(-1);
  cellIdx.setAttribute('align', 'right');
  cellIdx.innerHTML = `${idx - 1}.`;

  const cellName = row.insertCell(-1);
  cellName.innerHTML = `<input type="text" id="editBookName" placeholder="Title" class="form-control" value="${book.name}" autofocus="autofocus" />`;

  const cellAuthor = row.insertCell(-1);
  cellAuthor.innerHTML = `<input type="text" id="editBookAuthor" placeholder="Author" class="form-control" value="${book.author}" />`;

  const cellGenre = row.insertCell(-1);
  cellGenre.innerHTML = `<input type="text" id="editBookGenre" placeholder="Genre" class="form-control" value="${book.genre}" />`;

  const cellComments = row.insertCell(-1);
  cellComments.innerHTML = `<input type="text" id="editBookComments" placeholder="Comments" class="form-control" value="${book.comments}" />`;

  const cellEditBtn = row.insertCell(-1);
  cellEditBtn.innerHTML = `<button type="submit" class="btn btn-primary form-control" onclick="saveBook('${book.name}')"><span class="glyphicon glyphicon-plus"></span> Save</button>`;
}

function saveBook(oldBookName) {
  const book = {};
  book.name = $("#editBookName").val();
  book.author = $("#editBookAuthor").val();
  book.genre = $("#editBookGenre").val();
  book.comments = $("#editBookComments").val();

  $.ajax({
    type: "PUT",
    url: booksApi + `/${oldBookName}`,
    data: JSON.stringify(book),
    contentType: "application/json; charset=utf-8",
    dataType: "json",
    success: function (receivedBook) {
      updateBookInList(oldBookName, receivedBook);
    },
    error: onError
  });
}

function deleteBook(bookName) {
  $.ajax({
    type: "DELETE",
    url: booksApi + `/${bookName}`,
    success: function () {
      removeUserFromList(bookName);
    },
    error: onError
  });
}

function insertBookInList(book, idx) {
  const row = booksTable.insertRow(idx);

  const rowIdAttr = document.createAttribute('id');
  rowIdAttr.value = book.name;
  row.attributes.setNamedItem(rowIdAttr);

  const cellIdx = row.insertCell(-1);
  cellIdx.setAttribute('align', 'right');
  cellIdx.innerHTML = `${idx - 1}.`;

  Object.values(book).map((value, index) => row.insertCell(index + 1).innerHTML = value);

  const cellEditBtn = row.insertCell(-1);
  cellEditBtn.innerHTML = `<button class="btn btn-warning" onclick="editBook('${book.name}')"><span class="glyphicon glyphicon-edit"></span> Edit</button>`;

  const cellDeleteBtn = row.insertCell(-1);
  cellDeleteBtn.innerHTML = `<button class="btn btn-danger" onclick="deleteBook('${book.name}')"><span class="glyphicon glyphicon-trash"></span> Delete</button>`;
}

function updateBookInList(oldBookName, book) {
  let row = booksTable.rows.namedItem(oldBookName);
  const idx = row.sectionRowIndex;

  row.remove();
  insertBookInList(book, idx);
}

function removeUserFromList(bookName) {
  const row = booksTable.rows.namedItem(bookName);
  const idx = row.sectionRowIndex;
  row.remove();
  for (let i = idx; i < booksTable.rows.length; i++) {
    booksTable.rows[i].cells[0].innerHTML = `${i - 1}.`;
  }
}

function onError(xhr, errorType, exception) {
  console.log(xhr.responseText);
  const p = document.createElement('p');
  p.appendChild(
    document.createTextNode(xhr.responseText)
  );
  document.body.insertBefore(p, document.body.children[1]);
}

<html>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="Knowledgebase 2.0">
    <meta name="author" content="Ronny Friedland">
    <title>Knowledgebase 2.0</title>
    <link rel="icon" href="/images/icon.gif" type="image/gif" />

    <link rel="stylesheet" href="/bootstrap.min.css">
    <link rel="stylesheet" href="/bootstrap-theme.min.css">
    <script src="/jquery-1.11.2.min.js"></script>
    <script src="/bootstrap.min.js"></script>
</head>

<body role="document">
  <nav class="navbar navbar-default">
    <div class="container">
      <div class="navbar-header">
        <a class="navbar-brand" href="/">${locale("app.name")}</a>
      </div>
      <div class="navbar-collapse">
        <ul class="nav navbar-nav">
        <li><a href="/data">${locale("app.menu.list")}</a></li>
        <li><a href="/data/add">${locale("app.menu.add")}</a></li>
        </ul>
      </div><!--/.nav-collapse -->
    </div>
  </nav>

  <div class="container" role="main">

    <div class="alert alert-warning">
      ${locale("app.text.import.hint")}
    </div>
  
    <div class="panel panel-default">
      <div class="panel-heading">${locale("app.header.import")}</div>
      <div class="panel-body">

        <form role="form" id="importForm" action="/data/xml/import" method="post">
          <div class="form-group"><label for="importFile">${locale("app.label.importfile")}</label><br/><input type="file" name="importFile" id="importFile"/></div>
          <hr>
          <div class="form-group"><label for="importXml">${locale("app.label.importtext")}</label><br/><textarea class="form-control" name="importXml" id="importXml"></textarea></div>
          <input class="btn btn-default" type="submit" value="${locale("app.link.import")}" />
        </form>

      </div>
    </div>
  </div>
  <div class="container">
    <footer>
      <p>Version: ${project.version}</p>
    </footer>
  </div>
</body>

</html>
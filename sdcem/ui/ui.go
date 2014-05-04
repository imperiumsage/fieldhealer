package ui

import (
  "fmt"
  "net/http"

  "appengine"
  "appengine/user"
)

func init() {
  http.HandleFunc("/sdcem/ui", handler)
}

func handler(w http.ResponseWriter, r *http.Request) {
  c := appengine.NewContext(r)
  u := user.Current(c) 
  if u == nil {
    url, err := user.LoginURL(c, r.URL.String())
    if err != nil {
        http.Error(w, err.Error(), http.StatusInternalServerError)
        return
    }
    w.Header().Set("Location", url)
    w.WriteHeader(http.StatusFound)
    return
  }
  fmt.Fprint(w, templateHTML)
}

const templateHTML = `
<!doctype html>
<html>
<head>
<script src="/sdcem/js/library.js"></script>
<link rel="stylesheet" type="text/css" href="/sdcem/css/library.css">
</head>
<body>
<div class="lhs"></div>
<div class="top"></div>
</body>
</html>
`
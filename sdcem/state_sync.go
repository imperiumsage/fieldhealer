package state_sync

import (
  "fmt"
  "net/http"

  "appengine"
  "appengine/user"
)

func init() {
  http.HandleFunc("/sdcem/get_all", handler)
}

type StateType struct {
  Id int
  Value string
}

type BasicTrigger struct {
  Variable string
  Operator int
  Value string
}

type TriggerType struct {
  Basic_trigger BasicTrigger
  Logical_operation int
  Trigger []TriggerType
}

type StateTransitionType struct {
  Begin_state int
  End_state int
  Trigger TriggerType
}

type AllData struct {
  State []StateType
  State_transition []StateTransitionType
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
  b := `Response {"state": [ {"id" : 0, "value" : "START"}, {"id" : 1, "value" : "cough"}, {"id" : 2, "value" : "common cold"}], "state_transition" : [{ "begin_state" : 1, "end_state" : 2, "trigger" : {"basic_trigger" : {"variable" : "duration", "operator" : 2, "value" : "3 weeks"}}}]}`
  w.Header().Set("Content-Type", "application/json")
  fmt.Fprint(w, b)
  return
}

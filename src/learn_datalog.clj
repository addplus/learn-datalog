; Source: https://gist.github.com/fasiha/2ab2c1cb203c26a2b63532831f1b6021#file-learn_datalog_today-clj

; Learn Datalog Today (http://www.learndatalogtoday.org) is a great resource for
; reading but its interactive query interface is broken. Below is how we can
; load the same data into a Clojure REPL and play with it using DataScript.
; After running the code below, many/most/all? of the queries on Learn Datalog
; Today should be functional.
;
; Create a new lein project, add `[datascript "0.15.0"]` to `project.clj`'s
; `dependencies`, run `lein deps && lein repl` and copy-paste the following in
; chunks, inspecting the outputs as needed.

(ns learn-datalog
  (:require [datascript.core :as d]
            [clojure.string :as string]))

; raw Github assets for schema and data
(def data-url "https://raw.githubusercontent.com/jonase/learndatalogtoday/master/resources/db/data.edn")
(def schema-url "https://raw.githubusercontent.com/jonase/learndatalogtoday/master/resources/db/schema.edn")

; download data, remove the things that offends DataScript, and parse
(def data
  (-> data-url
      slurp
      (string/replace,,, #"#db/id \[:db\.part/user ([-\d]+)\]" "$1")
      read-string))

; download schema & slice and dice it into the format DataScript wants
(def schema
  (-> schema-url
      slurp
      ; DataScript doesn't need :db/id in as attributes
      (string/replace,,, #":db/id #db/id \[:db\.part/db\]" "")
      ; it definitely doesn't need to install attributes
      (string/replace,,, #":db\.install/_attribute :db\.part/db" "")
      ; it doesn't like being told about any value types other than refs
      (string/replace,,, #":db/valueType :db\.type/(string|long|instant)" "")
      read-string
      ; instead of a vector of schemas, DataScript wants a nested map
      (->>,,,
        (map (fn [{ident :db/ident :as schema}]
               (let [others (dissoc schema :db/ident)]
                 {ident others})),,,)
        (apply merge,,,))))

; create a DataScript db using the schema and dump the data into it
(def conn (d/create-conn schema))
(d/transact! conn data)

; try a query
(d/q '[:find ?title
       :where
       [_ :movie/title ?title]]
     @conn)
; #{["First Blood"] ["Terminator 2: Judgment Day"] ["The Terminator"] …

(d/q '[:find ?e
       :where
       [?e :person/name "Ridley Scott"]]
     @conn)
; #{[38]}

(d/q '[:find ?title
       :where
       [?e :movie/title ?title]
       [?e :movie/year 1987]]
     @conn)
; #{["Lethal Weapon"] ["RoboCop"] ["Predator"]}

(d/q '[:find ?name
       :where
       [?m :movie/title "Lethal Weapon"]
       [?m :movie/cast ?p]
       [?p :person/name ?name]]
     @conn)
; #{["Danny Glover"] ["Gary Busey"] ["Mel Gibson"]}